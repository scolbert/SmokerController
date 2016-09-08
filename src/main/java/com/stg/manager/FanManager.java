package com.stg.manager;

import java.util.Collection;
import java.util.OptionalDouble;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;

@Component
public class FanManager {

	Log logger = LogFactory.getLog(getClass());

	@Value("${fan.manager.min.fan.value}")
	@VisibleForTesting
	Integer MIN_FAN_VALUE;

	@Value("${fan.manager.max.fan.value}")
	@VisibleForTesting
	Integer MAX_FAN_VALUE;

	@Value("${fan.manager.temperature.average.history.count}")
	@VisibleForTesting
	Integer TEMP_HISTORY_COUNT;

	@Value("${fan.manager.temperature.average.history.count.minimum}")
	@VisibleForTesting
	Integer TEMP_HISTORY_COUNT_MIN;

	@Value("${fan.manager.distance.threshold}")
	@VisibleForTesting
	Integer TEMP_DIST_THRESHOLD;

	@VisibleForTesting
	Queue<Temperature> tempHistory = new PriorityQueue<>();

	public Integer calculateFanValue(Temperature currentTemp, Temperature targetTemp, Integer currentFanValue) {
		Integer newFanSetting = currentFanValue;

		Double average = average(tempHistory);
		;
		if (tempHistory.size() < TEMP_HISTORY_COUNT_MIN || average <= 0) {
			logger.info("Not enough readings to judge history.  " + average + " : " + tempHistory.size());
			if (currentTemp.getTemp(Scale.KELVIN) < targetTemp.getTemp(Scale.KELVIN)) {
				newFanSetting = 128;
			} else {
				if (currentFanValue > 0) {
					newFanSetting = currentFanValue / 2;
				} else {
					newFanSetting = 1;
				}
			}
		} else {

			Double change = currentTemp.getTemp(Scale.KELVIN) - average;
			Double difference = (double) targetTemp.getTemp(Scale.KELVIN) - currentTemp.getTemp(Scale.KELVIN);

			if (Math.signum(change) == Math.signum(difference)) {
				// we are going the right direction
				if (difference / change > TEMP_DIST_THRESHOLD) {
					// we are getting there too slowly, lets change the fan
					if (logger.isDebugEnabled()) {
						logger.debug("Getting there too slowly, change : difference" + change + " : " + difference);
					}
					newFanSetting += difference.intValue();
				} else {
					// looking good, change it just a touch.
					if (logger.isDebugEnabled()) {
						logger.debug("Getting there just right, changing by " + ((int) Math.signum(difference)));
					}
					newFanSetting += (int) Math.signum(difference);
				}
			} else {
				// going the wrong direction
				if (logger.isDebugEnabled()) {
					logger.debug("Wrong way.  changing fan by " + difference.intValue());
				}

				newFanSetting += difference.intValue();
			}
		}

		if (newFanSetting > MAX_FAN_VALUE) {
			newFanSetting = MAX_FAN_VALUE;
		} else if (newFanSetting < MIN_FAN_VALUE) {
			newFanSetting = MIN_FAN_VALUE;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("FanValueCalulator: " + currentTemp + " : " + targetTemp + " : " + average + " : "
					+ currentFanValue + " : " + newFanSetting);
		}

		if (tempHistory.size() > TEMP_HISTORY_COUNT) {
			tempHistory.poll();
		}
		tempHistory.offer(currentTemp);

		return newFanSetting;

	}

	@VisibleForTesting
	Double average(Collection<Temperature> list) {
		OptionalDouble avg = list.stream().mapToDouble(a -> a.getTemp(Scale.KELVIN)).average();
		if (avg.isPresent()) {
			return avg.getAsDouble();
		} else {
			return -1d;
		}
	}
}
