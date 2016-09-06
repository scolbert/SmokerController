package com.stg.io;

import java.io.IOException;
import java.util.Map;

/**
 * This class has 2 known implementations: HardwareControllerInterfaceImpl and
 * DummyHardwareInterfaceImpl. Use DummyHardwareInterfaceImpl when running on
 * non-Raspberry Pi hardware. --spring.profiles.active=dev or test on the
 * command line will cause spring to use the Dummy interface.
 */
public interface Hardware {

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

	Map<Integer, Integer> calibrate(Integer temp)
			throws NumberFormatException, IllegalStateException, IOException, InterruptedException;

}