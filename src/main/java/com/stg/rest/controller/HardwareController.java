package com.stg.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.io.HardwareInterface;
import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;

@RestController
@RequestMapping("api/v1/Hardware")
public class HardwareController {
	
	@Autowired
	private HardwareInterface smoker;

	@RequestMapping(value = "Probe/{id}", method = RequestMethod.GET)
	public Temperature getProbeReading(@PathVariable Integer id) {
		if (id > 0 && id < 5) {
			return new Temperature(smoker.getTemp(id), Scale.KELVIN);
		} 
		return null;
	}
	
	@RequestMapping(value = "Fan", method = RequestMethod.GET)
	public Integer getFanSetting() {
		return smoker.getFanSetting();
	}
	
	@RequestMapping(value = "Fan/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
	public Integer setFan(@PathVariable Integer id) {
		smoker.setFan(id);
		return smoker.getFanSetting();
	}
	
	@RequestMapping(value = "Light", method = RequestMethod.GET)
	public boolean changeLight() {
		return smoker.changeSessionLight();
	}
	
	@RequestMapping(value = "Light/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
	public boolean setLight(@PathVariable Integer id) {
		return smoker.setSessionLight(id > 0);
	}
}
