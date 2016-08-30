package com.stg.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HardwareCalulator {

	static Log logger = LogFactory.getLog(HardwareCalulator.class);
	
	static Integer calculateBeta(Integer targetTemp, Double avgReading, int initialBeta, long resistor, long thermometer_nominal_resistance) {
		logger.debug("Calculating Beta from " + avgReading + ".  Target Temp: " + targetTemp);
		if (avgReading < 0) {
			return -1;
		}
		int increment = 10;
		double priorDiff = Double.MAX_VALUE;
		int beta = initialBeta;
		double temp;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Initial beta: " + beta + " initial temp: " + getTempFromSmokerOutput(avgReading, beta, resistor, thermometer_nominal_resistance));
		}
		
		do {
			temp = getTempFromSmokerOutput(avgReading, beta, resistor, thermometer_nominal_resistance)	;
			//logger.debug("Prior Diff: " + priorDiff + " Current Diff: " + (Math.abs(targetTemp - temp)) + " temp: " + temp + " beta: " + beta);
			if (Math.abs(targetTemp - temp) < priorDiff) {
				// getting closer
				beta += increment * Math.signum(temp - targetTemp);
				priorDiff = Math.abs(targetTemp - temp);
			} else {
				logger.debug("Prior Diff: " + priorDiff + " Current Diff: " + (Math.abs(targetTemp - temp)) + " temp: " + temp + " beta: " + beta);
				return beta;
			}
		} while (true);
	}
	
	public static double getTempFromSmokerOutput(double smokerOutput, int beta, long resistor, long thermometerResistance) {
		double resistance = resistor / (1023 / smokerOutput - 1);
		// https://learn.adafruit.com/thermistor/using-a-thermistor
		// steinhart equation
		double kelvin = resistance / thermometerResistance;
		kelvin = Math.log(kelvin);
		kelvin /= beta;
		kelvin += 1.0d / (25 + 273.15d);
		kelvin = 1d / kelvin;
		return kelvin;
	}
}
