package com.stg.manager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;


public class FanManagerTest {

	private FanManager fanMgr = new FanManager();
	
	@Before
	public void init() {
		fanMgr.MAX_FAN_VALUE = 255;
		fanMgr.MIN_FAN_VALUE = 0;
		fanMgr.TEMP_HISTORY_COUNT = 5;
		fanMgr.TEMP_HISTORY_COUNT_MIN = 4;
		fanMgr.TEMP_DIST_THRESHOLD = 5;
	}
	

	@Test
	public void testStartupCold() {
		assertEquals(new Integer(128), fanMgr.calculateFanValue(new Temperature(0, Scale.KELVIN), new Temperature(300, Scale.KELVIN), 0));
	}
	
	@Test
	public void testStartupHot() {
		assertEquals(new Integer(100), fanMgr.calculateFanValue(new Temperature(300, Scale.KELVIN), new Temperature(0, Scale.KELVIN), 200));
		assertEquals(new Integer(1), fanMgr.calculateFanValue(new Temperature(300, Scale.KELVIN), new Temperature(0, Scale.KELVIN), 0));
	}
	
	@Test
	public void testRunWarmingUp1() {
		setAverage(new Temperature(295, Scale.KELVIN));
		assertEquals(new Integer(205), fanMgr.calculateFanValue(new Temperature(300, Scale.KELVIN), new Temperature(377, Scale.KELVIN), 128));
	}
	
	@Test
	public void testRunWarmingUp2() {
		setAverage(new Temperature(295, Scale.KELVIN));
		assertEquals(new Integer(129), fanMgr.calculateFanValue(new Temperature(320, Scale.KELVIN), new Temperature(377, Scale.KELVIN), 128));
	}
	
	@Test
	public void testRunCoolingDown1() {
		setAverage(new Temperature(400, Scale.KELVIN));
		assertEquals(new Integer(106), fanMgr.calculateFanValue(new Temperature(399, Scale.KELVIN), new Temperature(377, Scale.KELVIN), 128));
	}
	
	@Test
	public void testRunCoolingDown2() {
		setAverage(new Temperature(400, Scale.KELVIN));
		assertEquals(new Integer(127), fanMgr.calculateFanValue(new Temperature(395, Scale.KELVIN), new Temperature(377, Scale.KELVIN), 128));
	}
	
	@Test
	public void testRunWrongWay() {
		setAverage(new Temperature(380, Scale.KELVIN));
		assertEquals(new Integer(130), fanMgr.calculateFanValue(new Temperature(375, Scale.KELVIN), new Temperature(377, Scale.KELVIN), 128));
	}
	
	private void setAverage(Temperature temp) {
		fanMgr.tempHistory.clear();
		fanMgr.tempHistory.offer(temp);
		fanMgr.tempHistory.offer(temp);
		fanMgr.tempHistory.offer(temp);
		fanMgr.tempHistory.offer(temp);
		fanMgr.tempHistory.offer(temp);
	}

}
