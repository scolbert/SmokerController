package com.stg.io;

import java.io.IOException;

public interface HardwareInterface {

	Double getTemp(Integer input);

	void setFan(Integer value);

	Integer getFanSetting();
	
	String getTemperatureScale();
	
	boolean setSessionLight(boolean on);
	
	boolean changeSessionLight();

	void init() throws IOException;

}