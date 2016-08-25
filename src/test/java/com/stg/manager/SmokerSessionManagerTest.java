package com.stg.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stg.AppConfig;
import com.stg.manager.SmokerSessionManager;
import com.stg.model.SmokeSession;
import com.stg.model.Temperature;
import com.stg.model.TemperatureTiming;
import com.stg.model.TemperatureTimingDetail;
import com.stg.repository.SmokeSessionDetailRepository;
import com.stg.repository.SmokeSessionRepository;
import com.stg.model.Temperature.Scale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@ActiveProfiles(profiles = "test")

public class SmokerSessionManagerTest {
	
	@InjectMocks
	SmokeSessionDetailRepository smokeSessionDetailRepository;
	
	@InjectMocks
	SmokeSessionRepository smokeSessionRepository;

	@Autowired
	SmokerSessionManager manager;
	
	@Test
	public void testNoActiveSession() {
		assertFalse(manager.isSessionInProgress());
	}
	
	@Test
	public void testNewSession() {
		SmokeSession session = new SmokeSession();
		session.setReferenceThermometer(1);
		
		TemperatureTiming timings = new TemperatureTiming();
		//session.setTemperatureTimingId(timings);
		
		List<TemperatureTimingDetail> timingDetails = new ArrayList<>();
		timings.setTempDetails(timingDetails);
		TemperatureTimingDetail detail = new TemperatureTimingDetail();
		detail.setMinutesAtTemp(0);
		detail.setOrder(0);
		detail.setTemperature(new Temperature(330, Scale.KELVIN));
		timingDetails.add(detail);
		
		manager.startSession(session);
		assertTrue(manager.isSessionInProgress());
		assertEquals(session, manager.getActiveSession());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.stopSession();
		assertFalse(manager.isSessionInProgress());
	}
	
	@Test
	public void testNewSessionWithOffCrit() {
		SmokeSession session = new SmokeSession();
		session.setReferenceThermometer(1);
		
		TemperatureTiming timings = new TemperatureTiming();
		//session.setTemperatureTimingId(timings);
		
		List<TemperatureTimingDetail> timingDetails = new ArrayList<>();
		timings.setTempDetails(timingDetails);
		TemperatureTimingDetail detail = new TemperatureTimingDetail();
		detail.setMinutesAtTemp(0);
		detail.setOrder(0);
		detail.setTemperature(new Temperature(330, Scale.KELVIN));
		timingDetails.add(detail);
		
		manager.startSession(session);
		assertTrue(manager.isSessionInProgress());
		assertEquals(session, manager.getActiveSession());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.stopSession();
		assertFalse(manager.isSessionInProgress());
	}
}
