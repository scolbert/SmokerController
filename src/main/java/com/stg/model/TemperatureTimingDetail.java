package com.stg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class TemperatureTimingDetail implements Comparable<TemperatureTimingDetail>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Temperature temperature;
	private Integer minutesAtTemp;
	private Integer cookOrder;
	
	@ManyToOne
	private TurnOffCriteria turnOffCriteria;
	
	@ManyToOne
	@JsonBackReference
	private TemperatureTiming temperatureTiming;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	public Integer getMinutesAtTemp() {
		return minutesAtTemp;
	}

	public void setMinutesAtTemp(Integer minutesAtTemp) {
		this.minutesAtTemp = minutesAtTemp;
	}

	public Integer getOrder() {
		return cookOrder;
	}

	public void setOrder(Integer order) {
		this.cookOrder = order;
	}

	public TemperatureTiming getTemperatureTiming() {
		return temperatureTiming;
	}

	public void setTemperatureTiming(TemperatureTiming temperatureTiming) {
		this.temperatureTiming = temperatureTiming;
	}

	public TurnOffCriteria getTurnOffCriteria() {
		return turnOffCriteria;
	}

	public void setTurnOffCriteria(TurnOffCriteria turnOffCriteria) {
		this.turnOffCriteria = turnOffCriteria;
	} 

	@Override
	public int compareTo(TemperatureTimingDetail o) {
		if (this.cookOrder.equals(o.cookOrder)) {
			return 0;
		} else if (this.cookOrder > o.cookOrder) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "TemperatureTimingDetail [id=" + id + ", temperature=" + temperature + ", minutesAtTemp=" + minutesAtTemp
				+ ", cookOrder=" + cookOrder + ", temperatureTiming=" + temperatureTiming + "]";
	}

}
