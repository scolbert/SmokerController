package com.stg.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class SmokeSessionDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonBackReference
	@JoinColumn(name = "SMOKE_SESSION")
	@ManyToOne
	private SmokeSession session;
	private Integer fan;
	private Temperature thermometer1;
	private Temperature thermometer2;
	private Temperature thermometer3;
	private Temperature thermometer4;
	private Date time;
	@ManyToOne
	private TemperatureTimingDetail temperatureTimingDetail;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SmokeSession getSession() {
		return session;
	}
	public void setSession(SmokeSession session) {
		this.session = session;
	}
	public Integer getFan() {
		return fan;
	}
	public void setFan(Integer fan) {
		this.fan = fan;
	}
	public Temperature getThermometer1() {
		return thermometer1;
	}
	public void setThermometer1(Temperature thermometer1) {
		this.thermometer1 = thermometer1;
	}
	public Temperature getThermometer2() {
		return thermometer2;
	}
	public void setThermometer2(Temperature thermometer2) {
		this.thermometer2 = thermometer2;
	}
	public Temperature getThermometer3() {
		return thermometer3;
	}
	public void setThermometer3(Temperature thermometer3) {
		this.thermometer3 = thermometer3;
	}
	public Temperature getThermometer4() {
		return thermometer4;
	}
	public void setThermometer4(Temperature thermometer4) {
		this.thermometer4 = thermometer4;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public TemperatureTimingDetail getTemperatureTimingDetail() {
		return temperatureTimingDetail;
	}
	public void setTemperatureTimingDetail(TemperatureTimingDetail temperatureTimingDetail) {
		this.temperatureTimingDetail = temperatureTimingDetail;
	}
	@Override
	public String toString() {
		return "SmokeSessionDetail [id=" + id + ", session=" + session + ", fan=" + fan + ", thermometer1="
				+ thermometer1 + ", thermometer2=" + thermometer2 + ", thermometer3=" + thermometer3 + ", thermometer4="
				+ thermometer4 + ", time=" + time + ", temperatureTimingDetailId=" + temperatureTimingDetail.getId() + "]";
	}
	
	
}
