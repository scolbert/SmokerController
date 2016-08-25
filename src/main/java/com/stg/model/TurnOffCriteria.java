package com.stg.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.util.StringUtils;

@Entity
public class TurnOffCriteria {

	@Id
	private Long id;
	private Temperature targetTemperature;
	private String probes;

	/** Comma separated list of probes.
	 * @return
	 */
	public String getProbes() {
		return probes;
	}
	
	public List<Integer> getProbeList() {
		List<Integer> results = new ArrayList<>();
		if (StringUtils.isEmpty(probes)) {
			return results;
		} 
		String[] tokens = probes.split(",");
		for (String token : tokens) {
			results.add(new Integer(token));
		}
		return results;
	}

	public void setProbes(String probes) {
		this.probes = probes;
	}

	public Temperature getTargetTemperature() {
		return targetTemperature;
	}

	public void setTargetTemperature(Temperature targetTemperature) {
		this.targetTemperature = targetTemperature;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
