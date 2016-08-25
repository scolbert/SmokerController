package com.stg.io;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.stg.model.Temperature;

@Component("smokerHardwareController")
@Profile({ "dev", "test" })
public class DummyHardwareControllerImpl implements HardwareInterface {

	Integer fan = 0;
	boolean light = false;
	Log logger = LogFactory.getLog(getClass());

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

}
