package com.stg.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class HardwareInterfaceImplTest {

	@Test
	public void testCalibrate() {
		int newBeta = HardwareCalulator.calculateBeta(300, 510d, 3000, 100000, 100000);
		double temp = HardwareCalulator.getTempFromSmokerOutput(510, newBeta, 100000, 100000);
		System.err.println(newBeta);
		assertEquals(300, temp, .5);
	}
	

	@Test
	public void testCalibrate2() {
		int newBeta = HardwareCalulator.calculateBeta(301, 510d, 3000, 100000, 100000);
		double temp = HardwareCalulator.getTempFromSmokerOutput(510, newBeta, 100000, 100000);
		System.err.println(newBeta);
		assertEquals(301, temp, .5);
	}
	

	@Test
	public void testCalibrate3() {
		int newBeta = HardwareCalulator.calculateBeta(299, 510d, 6000, 100000, 100000);
		double temp = HardwareCalulator.getTempFromSmokerOutput(510, newBeta, 100000, 100000);
		System.err.println(newBeta);
		assertEquals(299, temp, .5);
	}
	

	@Test
	public void testCalibrate4() {
		int newBeta = HardwareCalulator.calculateBeta(368, 69d, 6000, 100000, 100000);
		double temp = HardwareCalulator.getTempFromSmokerOutput(69, newBeta, 100000, 100000);
		System.err.println(newBeta);
		assertEquals(368, temp, .5);
	}

}
