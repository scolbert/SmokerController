package com.stg;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.stg.io.HardwareInterface;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	HardwareInterface smoker;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		try {
			smoker.init();
		} catch (IOException e) {
			// this is as bad as it gets.  If we can't connect to the controller, can't do anything
			e.printStackTrace();
			System.exit(1);
		}
	}

}
