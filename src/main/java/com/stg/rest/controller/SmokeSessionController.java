package com.stg.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.manager.SmokerSessionManager;
import com.stg.model.SmokeSession;
import com.stg.model.SmokeSessionDetail;
import com.stg.repository.SmokeSessionRepository;

@RestController
@RequestMapping("api/v1/smoke_session")
public class SmokeSessionController {
	
	@Autowired
	private SmokeSessionRepository smokeSessionRepository;
	
	@Autowired
	SmokerSessionManager smokerSessionManager;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<SmokeSession> list() {
		return smokeSessionRepository.findAll();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public SmokeSession findOne(@PathVariable Long id) {
		if (id == -1) {
			return smokerSessionManager.getActiveSession();
		}
		return smokeSessionRepository.findOne(id);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<SmokeSession> create(@RequestBody SmokeSession session) {
		if (smokerSessionManager.isSessionInProgress()) {
			return new ResponseEntity<>(smokerSessionManager.getActiveSession(), HttpStatus.FORBIDDEN);
		}
		session.setId(null); // make sure we are creating a new instance
		session.setSmokeSessionDetail(new ArrayList<SmokeSessionDetail>()); // make sure we arent getting a stale detail list
		session.setStartDate(new Date());
		SmokeSession currentSession = smokeSessionRepository.saveAndFlush(session);
		smokerSessionManager.startSession(currentSession);		
		return new ResponseEntity<>(currentSession, HttpStatus.OK);
	}

	@RequestMapping(value = "{id}/STOP", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
	public ResponseEntity<SmokeSession> stop() {
		return new ResponseEntity<>(smokerSessionManager.stopSession(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	public SmokeSession updateDescription(@PathVariable Long id, @RequestBody SmokeSession session) {
		SmokeSession existingSession = smokeSessionRepository.findOne(id);
		existingSession.setDescription(session.getDescription());
		return smokeSessionRepository.saveAndFlush(existingSession);
	}
	
	@RequestMapping(value = "{id}/details", method = RequestMethod.GET)
	public List<SmokeSessionDetail> getDetails(@PathVariable Long id) {
		return smokeSessionRepository.findOne(id).getSmokeSessionDetail();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public SmokeSession delete(@PathVariable Long id) {
		SmokeSession existingSession = smokeSessionRepository.findOne(id);
		smokeSessionRepository.delete(existingSession);
		return existingSession;
	}
	
	
	/**
	 * @return List of meats defined that have been previously been cooked
	 */
	@RequestMapping(value = "meat", method = RequestMethod.GET)
	public List<String> getMeats() {
		return smokeSessionRepository.getMeats();
	}
}
