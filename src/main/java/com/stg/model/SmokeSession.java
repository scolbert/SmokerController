package com.stg.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class SmokeSession {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String meat;
	private Integer referenceThermometer;
	private Date startDate;
	private String description;
	private Long temperatureTiming;

	@OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties
	private List<SmokeSessionDetail> smokeSessionDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeat() {
		return meat;
	}

	public void setMeat(String meat) {
		this.meat = meat;
	}

	public Integer getReferenceThermometer() {
		return referenceThermometer;
	}

	public void setReferenceThermometer(Integer referenceThermometer) {
		this.referenceThermometer = referenceThermometer;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SmokeSessionDetail> getSmokeSessionDetail() {
		return smokeSessionDetail;
	}

	public void setSmokeSessionDetail(List<SmokeSessionDetail> smokeSessionDetail) {
		this.smokeSessionDetail = smokeSessionDetail;
	}

	public Long getTemperatureTimingId() {
		return temperatureTiming;
	}

	public void setTemperatureTimingId(Long temperatureTiming) {
		this.temperatureTiming = temperatureTiming;
	}
	
}