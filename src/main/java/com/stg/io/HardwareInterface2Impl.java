package com.stg.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

@Component
@Primary
@Profile("default")
public class HardwareInterface2Impl implements HardwareInterface {

	Log logger = LogFactory.getLog(getClass());

	@Value("${thermometer.nominal.resistance}")
	private long THERMOMETER_NOMINAL;

	@Value("${thermometer.calibration.beta}")
	private int BETA;

	@Value("${thermometer.resistor.resistance}")
	private long RESISTOR;

	@Value("${serial.port.path}")
	private String serialPortPath;

	@Value("${serial.port.baud.rate}")
	private int baud;

	@Value("${arduino.read.sleep.time}")
	private int sleep;

	@Value("${arduino.read.sleep.count}")
	private int timesToRetry;

	@Value("${min.fan.value}")
	private Integer minFanValue;

	@Value("${max.fan.value}")
	private Integer maxFanValue;

	@Value("${hardware.calibration.sample.count}")
	private int calibrationSampleCount;

	private volatile boolean initialized = false;

	private Integer lastFanValue = 0;

	private Serial serialPort;
	private BufferedReader responseReader;
	private boolean light = false;

	@Override
	public synchronized void init() throws IOException {
		if (initialized) {
			return;
		}
		serialPort = SerialFactory.createInstance();
		logger.info("connecting to port at: " + serialPortPath + " at " + baud + " baud.");
		if (!serialPort.isOpen()) {
			serialPort.open(serialPortPath, baud);
		}
		responseReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

		initialized = true;
		try {
			// Turn the light on to indicate we are ready to take commands
			if (setSessionLight(true)) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.warn("Sleep interupted during INIT: " + e);
				}
				setSessionLight(true);
			}

		} catch (IllegalStateException e) {
			logger.error("Invalid response from hardware: " + e);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				logger.warn("Sleep interupted during INIT: " + e);
			}
			try {
				setSessionLight(true);
			} catch (IllegalStateException e1) {
				logger.fatal(
						"Invalid response from hardware for the second time.  Giving up.  Consider rebooting: " + e);
			}
		}
	}

	@PreDestroy
	public void cleanUp() {
		try {
			if (serialPort.isOpen()) {
				// try to shut off the fan and turn off the light if the app
				// closes.
				setFan(0);
				setSessionLight(false);
				serialPort.close();
			}
		} catch (IllegalStateException | IOException e) {
			logger.error("Exception closing the serial port", e);
		}
	}

	@Override
	public Double getTemp(Integer input) {
		String response;
		try {
			response = sendReceive(input.toString()).get(input.toString());
		} catch (IllegalStateException | IOException | InterruptedException e) {
			logger.error("Exception retreiving probe data: " + input, e);
			return -1d;
		}
		if (StringUtils.hasText(response)) {
			return getTempFromSmokerOutput(new Double(response));
		}
		return -1d;
	}

	@Override
	public Map<Integer, Double> getTemps() {
		Map<String, String> temps = new HashMap<>();
		Map<Integer, Double> response = new HashMap<>();
		try {
			temps = sendReceive("1,2,3,4");

			temps.forEach((k, v) -> {
				if (k.equals("1") || k.equals("2") || k.equals("3") || k.equals("4")) {
					response.put(new Integer(k), getTempFromSmokerOutput(new Double(v)));
				}
			});
		} catch (IllegalStateException | IOException | InterruptedException e) {
			logger.error("Exception retreiving probe data: ", e);
			return response;
		}
		return response;
	}

	@Override
	public void setFan(Integer value) {
		if (value > maxFanValue) {
			value = maxFanValue;
		}
		if (value < minFanValue) {
			value = minFanValue;
		}

		try {
			sendReceive("5=" + value.toString());
		} catch (IllegalStateException | IOException | InterruptedException e) {
			logger.error("Exception thrown: " + value, e);
		}
		lastFanValue = value;
	}

	@Override
	public Integer getFanSetting() {
		return lastFanValue;
	}

	@Override
	public String getTemperatureScale() {
		return "KELVIN";
	}

	@Override
	public boolean setSessionLight(boolean on) {
		try {
			Map<String, String> response = sendReceive("6=" + (on ? "1" : "0"));
			light = response.get("6") != null && response.get("6").equals("1") ? true : false;
		} catch (IllegalStateException | IOException | InterruptedException e) {
			logger.error("Exception setting the light: " + on, e);
			light = false;
		}
		return light;
	}

	@Override
	public boolean changeSessionLight() {
		return setSessionLight(!light);
	}

	/**
	 * Send a message to the Arduino over the provided serial port. Waits for a
	 * response. return an empty string if no response is received in time.
	 * 
	 * @param send
	 * @return response from the Arduino
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	synchronized Map<String, String> sendReceive(String send)
			throws IllegalStateException, IOException, InterruptedException {
		Map<String, String> results = new HashMap<>();

		if (!initialized) {
			this.init();
		}

		if (send.charAt(send.length() - 1) != ';') {
			send += ";"; // make sure we have a trailing ;
		}
		serialPort.write(send);
		serialPort.flush();
		for (int loop = 0; loop < timesToRetry && !responseReader.ready(); loop++) {
			Thread.sleep(sleep);
			if (logger.isTraceEnabled()) {
				logger.trace("No response yet from Arduino.  Sleeping for the " + (loop + 1) + " time");
			}
		}
		if (responseReader.ready()) {
			String response = responseReader.readLine();
			if (logger.isInfoEnabled()) {
				logger.info("Communication with Arduino: " + send + " : " + response);
			}
			if (StringUtils.hasText(response)) {
				if (response.charAt(response.length() - 1) != ';') {
					logger.error("Invalid response from hardware, missing ; : " + response);
					return new HashMap<>();
				}
				// get rid of the trailing ";"
				response = response.substring(0, response.length() - 1);
				String[] cmdResults = response.split(",");
				for (String cmdResult : cmdResults) {
					String[] cmdParts = cmdResult.split("=");
					if (cmdParts.length == 2) {
						results.put(cmdParts[0], cmdParts[1]);
					} else {
						logger.error("Invalid command response from hardware: " + cmdResult);
					}
				}
			}
		} else {
			logger.warn("Failed to return message from Arduino: " + send);
		}
		return results;
	}

	double getTempFromSmokerOutput(double smokerOutput) {
		return HardwareCalulator.getTempFromSmokerOutput(smokerOutput, BETA, RESISTOR, THERMOMETER_NOMINAL);
	}

	@Override
	public int getProbeCalibration() {
		return BETA;
	}

	@Override
	public void setProbeCalibration(int beta) {
		BETA = beta;
	}

	@Override
	public Map<Integer, Integer> calibrate(Integer targetTemp)
			throws NumberFormatException, IllegalStateException, IOException, InterruptedException {
		Map<Integer, Integer> calibrationMap = new HashMap<>();
		List<List<Double>> probeReadings = new ArrayList<>();
		probeReadings.add(new ArrayList<>());
		probeReadings.add(new ArrayList<>());
		probeReadings.add(new ArrayList<>());
		probeReadings.add(new ArrayList<>());

		for (int sample = 0; sample < calibrationSampleCount; sample++) {
			Map<Integer, Double> temps = getTemps();
			temps.forEach((k, v) -> probeReadings.get(k - 1).add(v));
		}

		calibrationMap.put(1, HardwareCalulator.calculateBeta(targetTemp, average(probeReadings.get(0)), BETA, RESISTOR,
				THERMOMETER_NOMINAL));
		calibrationMap.put(2, HardwareCalulator.calculateBeta(targetTemp, average(probeReadings.get(1)), BETA, RESISTOR,
				THERMOMETER_NOMINAL));
		calibrationMap.put(3, HardwareCalulator.calculateBeta(targetTemp, average(probeReadings.get(2)), BETA, RESISTOR,
				THERMOMETER_NOMINAL));
		calibrationMap.put(4, HardwareCalulator.calculateBeta(targetTemp, average(probeReadings.get(3)), BETA, RESISTOR,
				THERMOMETER_NOMINAL));
		return calibrationMap;
	}

	private Double average(List<Double> list) {
		OptionalDouble avg = list.stream().mapToDouble(a -> a).average();
		if (avg.isPresent()) {
			return avg.getAsDouble();
		} else {
			return -1d;
		}
	}
}
