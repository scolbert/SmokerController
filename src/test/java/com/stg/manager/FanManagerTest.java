package com.stg.manager;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stg.AppConfig;
import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@ActiveProfiles(profiles = "test")
@TestPropertySource(locations="classpath:test.properties")
public class FanManagerTest {

	@Autowired
	private FanManager fanMgr;

	@Test
	public void testMaxValue() {
		assertEquals(new Integer(255), fanMgr.calculateFanValue(new Temperature(0, Scale.KELVIN), new Temperature(300, Scale.KELVIN), 0));
	}
	
	@Test
	public void testMinValue() {
		assertEquals(new Integer(0), fanMgr.calculateFanValue(new Temperature(300, Scale.KELVIN), new Temperature(0, Scale.KELVIN), 255));
	}

}
