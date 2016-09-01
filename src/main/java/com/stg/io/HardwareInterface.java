package com.stg.io;

import java.io.IOException;
import java.util.Map;

public interface HardwareInterface {
	
	Map<Integer, Double> getTemps();
	
	Double getTemp(Integer input);

	void setFan(Integer value);

	Integer getFanSetting();
	
	String getTemperatureScale();
	
	boolean setSessionLight(boolean on);
	
	boolean changeSessionLight();

	void init() throws IOException;

	void setProbeCalibration(int beta);

	int getProbeCalibration();

	Map<Integer, Integer> calibrate(Integer temp) throws NumberFormatException, IllegalStateException, IOException, InterruptedException;

}