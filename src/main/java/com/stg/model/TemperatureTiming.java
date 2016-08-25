package com.stg.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.core.annotation.Order;

@Entity
public class TemperatureTiming {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	
	@Order
	@OneToMany( mappedBy = "temperatureTiming", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<TemperatureTimingDetail> tempDetails;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<TemperatureTimingDetail> getTempDetails() {
		return tempDetails;
	}
	public void setTempDetails(List<TemperatureTimingDetail> tempDetails) {
		this.tempDetails = tempDetails;
	}
}
