package com.stg.rest.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.model.TemperatureTiming;
import com.stg.model.TemperatureTimingDetail;
import com.stg.repository.TemperatureTimingRepository;

@RestController
@RequestMapping("api/v1/TempTiming")
public class TemperatureTimingController {

	@Autowired
	private TemperatureTimingRepository timingRepo;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<TemperatureTiming> list() {
		return timingRepo.findAll();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public TemperatureTiming findOne(@PathVariable Long id) {
		return timingRepo.findOne(id);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public TemperatureTiming create(@RequestBody TemperatureTiming newTiming) {
		return timingRepo.saveAndFlush(newTiming);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public TemperatureTiming edit(@PathVariable Long id, @RequestBody TemperatureTiming editTiming) {
		TemperatureTiming existing = timingRepo.findOne(id);
		BeanUtils.copyProperties(editTiming, existing);
		return timingRepo.saveAndFlush(existing);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public TemperatureTiming delete(@PathVariable Long id) {
		TemperatureTiming timing = timingRepo.getOne(id);
		timingRepo.delete(timing);
		return timing;
	}

	@RequestMapping(value = "{id}/details", method = RequestMethod.GET)
	public List<TemperatureTimingDetail> getDetails(@PathVariable Long id) {
		return timingRepo.findOne(id).getTempDetails();
	}

	@RequestMapping(value = "{id}/details", method = RequestMethod.POST)
	public List<TemperatureTimingDetail> addDetail(@PathVariable Long id,
			@RequestBody TemperatureTimingDetail newDetail) {
		TemperatureTiming timing = timingRepo.findOne(id);
		List<TemperatureTimingDetail> details = timing.getTempDetails();
		// if no order is specified, just add it to the end or if there isnt any other details
		if (newDetail.getOrder() == null || details.isEmpty()) {
			newDetail.setOrder(details.size() + 1); // stupid human 1 based ordering....
			details.add(newDetail);
		} else {
			Collections.sort(details);
			for (TemperatureTimingDetail detail : details) {
				if (detail.getOrder() >= newDetail.getOrder()) {
					detail.setOrder(detail.getOrder() + 1);
				}
			}
		}
		timing.setTempDetails(details);
		timingRepo.saveAndFlush(timing);
		return timingRepo.findOne(id).getTempDetails(); 
	}

}
