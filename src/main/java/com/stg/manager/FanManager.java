package com.stg.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;

@Component
public class FanManager {

	Log logger = LogFactory.getLog(getClass());

	private boolean initialStartup = true;
	
	@Value("${min.fan.value}")
	private Integer minFanValue;
	@Value("${max.fan.value}")
	private Integer maxFanValue;
	
	public Integer calculateFanValue(Temperature currentTemp, Temperature targetTemp, Integer currentFanValue) {
		Integer newFanSetting = currentFanValue;

		newFanSetting += targetTemp.getTemp(Scale.KELVIN) - currentTemp.getTemp(Scale.KELVIN);
		
		if (newFanSetting > maxFanValue) {
			newFanSetting = maxFanValue;
		} else if (newFanSetting < minFanValue) {
			newFanSetting = minFanValue;
		}
		
		// for the initial startup case, we are going to get hot fast, so we need to reduce the fan setting quickly
		if (initialStartup && currentTemp.getTemp(Scale.KELVIN) > targetTemp.getTemp(Scale.KELVIN)) {
			initialStartup = false;
			newFanSetting /= 2;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("FanValueCalulator: " + currentTemp + " : " + targetTemp + " : " + currentFanValue + " : " + newFanSetting);
		}
		return newFanSetting;

	}
}
