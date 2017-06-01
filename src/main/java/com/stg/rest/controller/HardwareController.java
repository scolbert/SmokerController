package com.stg.rest.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.stg.io.Hardware;
import com.stg.model.Temperature;
import com.stg.model.Temperature.Scale;

@RestController
@RequestMapping("api/v1/Hardware")
public class HardwareController {

	@Autowired
	private Hardware smoker;

	@CrossOrigin
	@RequestMapping(value = "Probe/{id}", method = RequestMethod.GET)
	public Temperature getProbeReading(@PathVariable Integer id) {
		if (id > 0 && id < 5) {
			return new Temperature(smoker.getTemp(id), Scale.KELVIN);
		}
		return null;
	}

	@CrossOrigin
	@RequestMapping(value = "Probe", method = RequestMethod.GET)
	public Map<Integer, Temperature> getAllProbeReading() {
		Map<Integer, Temperature> results = new HashMap<>();
		smoker.getTemps().forEach((k,v) -> results.put(k, new Temperature(v, Scale.KELVIN)));
		return results;
	}

	@CrossOrigin
	@RequestMapping(value = "Fan", method = RequestMethod.GET)
	public Integer getFanSetting() {
		return smoker.getFanSetting();
	}

	@CrossOrigin
	@RequestMapping(value = "Fan/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
	public Integer setFan(@PathVariable Integer id) {
		smoker.setFan(id);
		return smoker.getFanSetting();
	}

	@CrossOrigin
	@RequestMapping(value = "Light", method = RequestMethod.GET)
	public boolean changeLight() {
		return smoker.changeSessionLight();
	}

	@CrossOrigin
	@RequestMapping(value = "Light/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
	public boolean setLight(@PathVariable Integer id) {
		return smoker.setSessionLight(id > 0);
	}
	

	/**
	 * @param temp - Temperature that the probes are currently at - typically 100c or 0 c
	 * @return new calibration constant.  This does NOT set it in the application.properties file.  You have to do that on your own.  If I ever get the settings db table working, you can also save it there.
	 * @throws NumberFormatException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@CrossOrigin
	@RequestMapping(value = "calibrate", method = { RequestMethod.PUT, RequestMethod.GET })
	public Map<Integer, Integer> calibrate(@RequestBody Temperature temp) throws NumberFormatException, IllegalStateException, IOException, InterruptedException {
		return smoker.calibrate(temp.getTemp(Scale.KELVIN));
	}
}
