package com.stg.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Settings {
	
	@Id
	private Long id;
	private String scale;
	private Integer ambientThermometer;

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
}
