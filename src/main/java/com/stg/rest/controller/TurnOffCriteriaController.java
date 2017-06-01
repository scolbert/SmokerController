package com.stg.rest.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.stg.model.TurnOffCriteria;
import com.stg.repository.TurnOffCriteriaRepository;

@RestController
@RequestMapping("api/v1/TurnOffCriteria")
public class TurnOffCriteriaController {
	
	@Autowired
	private TurnOffCriteriaRepository turnOffRepo;

	@CrossOrigin
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<TurnOffCriteria> list() {
		return turnOffRepo.findAll();
	}


	@CrossOrigin
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public TurnOffCriteria edit(@PathVariable Long id, @RequestBody TurnOffCriteria editTiming) {
		TurnOffCriteria existing = turnOffRepo.findOne(id);
		BeanUtils.copyProperties(editTiming, existing);
		return turnOffRepo.saveAndFlush(existing);
	}

	@CrossOrigin
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public TurnOffCriteria delete(@PathVariable Long id) {
		TurnOffCriteria turnOff = turnOffRepo.getOne(id);
		turnOffRepo.delete(turnOff);
		return turnOff;
	}
}
