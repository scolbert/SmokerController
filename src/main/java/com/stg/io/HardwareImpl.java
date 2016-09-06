package com.stg.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Profile("default")
public class HardwareImpl implements Hardware {

	Log logger = LogFactory.getLog(getClass());

	@Value("${thermometer.nominal.resistance}")
	private long THERMOMETER_NOMINAL;

	@Value("${thermometer.calibration.beta}")
	private int BETA;

	@Value("${thermometer.resistor.resistance}")
	private long RESISTOR;

	@Value("${min.fan.value}")
	private Integer minFanValue;

	@Value("${max.fan.value}")
	private Integer maxFanValue;

	@Value("${hardware.calibration.sample.count}")
	private int calibrationSampleCount;

	private Integer lastFanValue = 0;

	@Autowired
	HardwareCommunicator hwIO;

	private boolean light = false;

	@Override
	public synchronized void init() throws IOException {

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
		if (hwIO.isAvailable()) {
			// try to shut off the fan and turn off the light if the app closes.
			setFan(0);
			setSessionLight(false);
			hwIO.shutdown();
		}
	}

	@Override
	public Double getTemp(Integer input) {
		String response;
		Map<String, String> cmd = new HashMap<>();
		cmd.put(input.toString(), null);
		try {
			response = hwIO.sendReceive(cmd).get(input.toString());
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
		Map<String, String> cmd = new HashMap<>();
		cmd.put("1", null);
		cmd.put("2", null);
		cmd.put("3", null);
		cmd.put("4", null);
		
		try {
			temps = hwIO.sendReceive(cmd);

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
		Map<String, String> cmd = new HashMap<>();
		
		if (value > maxFanValue) {
			value = maxFanValue;
		}
		if (value < minFanValue) {
			value = minFanValue;
		}

		cmd.put("5", value.toString());
		
		try {
			hwIO.sendReceive(cmd);
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
		Map<String, String> cmd = new HashMap<>();
		cmd.put("6", on ? "1" : "0");
		try {
			Map<String, String> response = hwIO.sendReceive(cmd);
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
