package com.stg.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.stg.model.Temperature;

@Component
@Profile({ "dev", "test" })
public class DummyHardwareInterfaceImpl implements Hardware {

	Integer fan = 0;
	boolean light = false;
	Log logger = LogFactory.getLog(getClass());
	private int beta;

	@Override
	public void init() throws IOException {
		logger.info("DummySmokerHardwareControllerImpl initialized");
	}

	@PreDestroy
	public void cleanUp() {
		logger.info("DummySmokerHardwareControllerImpl shut down");
	}

	@Override
	public Double getTemp(Integer input) {
		Double temp = 300d + ((double) input / 10) + fan + ((int) (Math.random() * 40 - 20));
		logger.warn("Returning current Temp: " + temp);
		return temp;
	}

	@Override
	public void setFan(Integer value) {
		fan = value;
		logger.warn("Setting fan to: " + value);
	}

	@Override
	public Integer getFanSetting() {
		return fan;
	}

	@Override
	public String getTemperatureScale() {
		return Temperature.Scale.KELVIN.name();
	}

	@Override
	public boolean setSessionLight(boolean on) {
		light = on;
		return light;
	}

	@Override
	public boolean changeSessionLight() {
		light = !light;
		return light;
	}

	@Override
	public void setProbeCalibration(int beta) {
		this.beta = beta;
	}

	@Override
	public int getProbeCalibration() {
		return beta;
	}

	@Override
	public Map<Integer, Integer> calibrate(Integer temp) {
		Map<Integer, Integer> calibrationMap = new HashMap<>();
		calibrationMap.put(1, beta);
		calibrationMap.put(2, beta);
		calibrationMap.put(3, beta);
		calibrationMap.put(4, beta);
		return calibrationMap;
	}

	@Override
	public Map<Integer, Double> getTemps() {
		Map<Integer, Double> results = new HashMap<>();
		results.put(1, 300d + ((double) 1 / 10) + fan + ((int) (Math.random() * 40 - 20)));
		results.put(2, 300d + ((double) 2 / 10) + fan + ((int) (Math.random() * 40 - 20)));
		results.put(3, 300d + ((double) 3 / 10) + fan + ((int) (Math.random() * 40 - 20)));
		results.put(4, 300d + ((double) 4 / 10) + fan + ((int) (Math.random() * 40 - 20)));
		return results;
	}

}
