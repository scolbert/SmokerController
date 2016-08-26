package com.stg.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

@Component("smokerHardwareController")
@Profile("default")
public class HardwareControllerImpl implements HardwareInterface {

	Log logger = LogFactory.getLog(getClass());

	@Value("${thermometer.nominal.resistance}")
	private int THERMOMETER_NOMINAL;

	@Value("${thermometer.calibration.beta}")
	private int BETA;

	@Value("${thermometer.resistor.resistance}")
	private int RESISTOR;

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
			if (StringUtils.isEmpty(sendReceive("6,1"))) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.warn("Sleep interupted during INIT: " + e);
				}
				sendReceive("6,1");
			}

		} catch (IllegalStateException | InterruptedException e) {
			logger.error("Invalid response from hardware: " + e);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				logger.warn("Sleep interupted during INIT: " + e);
			}
			try {
				sendReceive("6,1");
			} catch (IllegalStateException | InterruptedException e1) {
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
			response = sendReceive(input.toString());
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
	public void setFan(Integer value) {
		if (value > maxFanValue) {
			value = maxFanValue;
		}
		if (value < minFanValue) {
			value = minFanValue;
		}

		try {
			sendReceive("5," + value.toString());
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
	synchronized String sendReceive(String send) throws IllegalStateException, IOException, InterruptedException {
		if (!initialized) {
			this.init();
		}
		serialPort.write(send);
		serialPort.flush();
		for (int loop = 0; loop < timesToRetry && !responseReader.ready(); loop++) {
			Thread.sleep(sleep);
			if (logger.isDebugEnabled()) {
				logger.trace("No response yet from Arduino.  Sleeping for the " + (loop + 1) + " time");
			}
		}
		if (responseReader.ready()) {
			String response = responseReader.readLine();
			if (logger.isInfoEnabled()) {
				logger.info("Communication with Arduino: " + send + " : " + response);
			}
			if (StringUtils.hasText(response)) {
				String[] tokens = response.split("=");
				if (tokens.length != 2) {
					// Houston, we have a problem
					logger.error("Invalid response from hardware: " + response);
					return "";
				}
				return tokens[1];
			}
		}
		logger.warn("Failed to return message from Arduino");
		return "";
	}

	double getTempFromSmokerOutput(double smokerOutput) {
		double resistance = RESISTOR / (1023 / smokerOutput - 1);
		// https://learn.adafruit.com/thermistor/using-a-thermistor
		// steinhart equation
		double kelvin = resistance / THERMOMETER_NOMINAL;
		kelvin = Math.log(kelvin);
		kelvin /= BETA;
		kelvin += 1.0d / (25 + 273.15d);
		kelvin = 1d / kelvin;
		return kelvin;
	}

	@Override
	public boolean setSessionLight(boolean on) {
		try {
			String response = sendReceive("6," + (on ? "1" : "0"));
			light = response.equals("1") ? true : false;
		} catch (IllegalStateException | IOException | InterruptedException e) {
			logger.error("Exception setting the light: " + on, e);
		}
		return light;
	}

	@Override
	public boolean changeSessionLight() {
		return setSessionLight(!light);
	}
}
