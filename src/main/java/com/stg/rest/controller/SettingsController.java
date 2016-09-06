//package com.stg.rest.controller;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.stg.model.Settings;
//import com.stg.model.Temperature;
//import com.stg.model.Temperature.Scale;
//import com.stg.repository.SettingsRepository;
//
//@RestController
//@RequestMapping("api/v1/Settings")
//public class SettingsController {
//
//	@Autowired
//	SettingsRepository settingsRepo;
//	
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public Settings getSettings() {
//		return settingsRepo.getOne(1L);
//	}
//
//	@RequestMapping(value = "", method = RequestMethod.PUT)
//	public Settings updateSettings(@RequestBody Settings updatedSettings) {
//		Settings currentSettings = settingsRepo.getOne(1L);
//		BeanUtils.copyProperties(updatedSettings, currentSettings);
//		currentSettings.setId(1L); // just to make sure there is no funny stuff going on.
//		settingsRepo.saveAndFlush(currentSettings);
//		return currentSettings;
//	}
//	
//	@RequestMapping(value="/scale", method = RequestMethod.GET)
//	public Scale[] getScales() {
//		return Temperature.Scale.values();
//	}
//}
