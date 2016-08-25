package com.stg.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.stg.model.Temperature.Scale;

public class TemperatureTest {

	@Test
	public void testCelsius() {
		Temperature temp = new Temperature(0, Scale.CELSIUS);
		assertEquals(new Integer(0), temp.getTemp(Scale.CELSIUS));
	}
	@Test
	public void testF() {
		Temperature temp = new Temperature(32, Scale.FAHRENHEIT);
		assertEquals(new Integer(32), temp.getTemp(Scale.FAHRENHEIT));
	}
	
	@Test
	public void testFboil() {
		Temperature temp = new Temperature(212, Scale.FAHRENHEIT);
		assertEquals(new Integer(212), temp.getTemp(Scale.FAHRENHEIT));
	}
	
	@Test
	public void testKelvin() {
		Temperature temp = new Temperature(273, Scale.KELVIN);
		assertEquals(new Integer(273), temp.getTemp(Scale.KELVIN));
	}

	@Test
	public void testCtoF() {
		Temperature temp = new Temperature(0, Scale.CELSIUS);
		assertEquals(new Integer(32), temp.getTemp(Scale.FAHRENHEIT));
	}
	@Test
	public void testEquals() {
		Temperature temp1 = new Temperature(0, Scale.CELSIUS);
		Temperature temp2 = new Temperature(273, Scale.KELVIN);
		assertEquals(temp1, temp2);
	}

	@Test
	public void testCompareEqual() {
		Temperature temp1 = new Temperature(0, Scale.CELSIUS);
		Temperature temp2 = new Temperature(273, Scale.KELVIN);
		assertEquals(0, temp1.compareTo(temp2));
	}
	@Test
	public void testCompareGreater() {
		Temperature temp1 = new Temperature(0, Scale.CELSIUS);
		Temperature temp2 = new Temperature(0, Scale.KELVIN);
		assertEquals(1, temp1.compareTo(temp2));
	}
	@Test
	public void testCompareLess() {
		Temperature temp1 = new Temperature(0, Scale.CELSIUS);
		Temperature temp2 = new Temperature(0, Scale.KELVIN);
		assertEquals(-1, temp2.compareTo(temp1));
	}
	@Test
	public void testNotEquals() {
		assertNotEquals(new Temperature(0, Scale.CELSIUS), 0);
		assertNotEquals(new Temperature(0, Scale.CELSIUS), null);
		assertNotEquals(new Temperature(0, Scale.CELSIUS), 0);
	}
}
