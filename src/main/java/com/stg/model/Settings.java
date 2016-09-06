package com.stg.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class is not used in the code.  All settings are currently managed by the application.properties files.
 * It is marked @deprecated until it is implemented fully.
 *
 */
@Entity
@Deprecated
public class Settings {
	
	@Id
	private Long id;
	private String scale;
	private Integer ambientThermometer;
	private Integer probe_beta;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Integer getAmbientThermometer() {
		return ambientThermometer;
	}

	public void setAmbientThermometer(Integer ambientThermometer) {
		this.ambientThermometer = ambientThermometer;
	}

	public Integer getProbe_calibration() {
		return probe_beta;
	}

	public void setProbe_calibration(Integer beta) {
		this.probe_beta = beta;
	}

}
