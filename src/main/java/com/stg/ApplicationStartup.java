package com.stg;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.stg.io.Hardware;
//import com.stg.model.Settings;
//import com.stg.repository.SettingsRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	Hardware smoker;
	
//	@Autowired
//	SettingsRepository settingsRepo;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		try {
//			Settings settings = settingsRepo.getOne(1l);
//			if (settings != null && settings.getProbe_beta() != null && settings.getProbe_beta() > 0) {
//				smoker.setProbeCalibration(settings.getProbe_beta());
//			}
			smoker.init();
		} catch (IOException e) {
			// this is as bad as it gets.  If we can't connect to the controller, can't do anything
			e.printStackTrace();
			System.exit(1);
		}
	}

}
