package com.stg.rest.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.model.TurnOffCriteria;
import com.stg.repository.TurnOffCriteriaRepository;

@RestController
@RequestMapping("api/v1/TurnOffCriteria")
public class TurnOffCriteriaController {
	
	@Autowired
	private TurnOffCriteriaRepository turnOffRepo;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<TurnOffCriteria> list() {
		return turnOffRepo.findAll();
	}
	

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public TurnOffCriteria edit(@PathVariable Long id, @RequestBody TurnOffCriteria editTiming) {
		TurnOffCriteria existing = turnOffRepo.findOne(id);
		BeanUtils.copyProperties(editTiming, existing);
		return turnOffRepo.saveAndFlush(existing);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public TurnOffCriteria delete(@PathVariable Long id) {
		TurnOffCriteria turnOff = turnOffRepo.getOne(id);
		turnOffRepo.delete(turnOff);
		return turnOff;
	}
}
