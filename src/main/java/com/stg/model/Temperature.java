package com.stg.model;

import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperature implements Comparable<Temperature> {

	@JsonProperty
	private Double tempK;

	public Temperature() {
	}

	public Temperature(Integer temp, Scale scale) {
		this.setTemp(temp, scale);
	}
	
	public Temperature(Double temp, Scale scale) {
		this.tempK = scale.toKelvin(temp);
	}

	public void setTemp(Integer temp, Scale scale) {
		this.tempK = scale.toKelvin(new Double(temp));
	}

	public Integer getTemp(Scale scale) {
		return new Double(Math.rint(scale.fromKelvin(tempK))).intValue();
	}

	@Override
	public String toString() {
		return "Temperature [tempK=" + tempK + "]";
	}
	
	Double getTemp() {
		return tempK;
	}
	
	void setTemp(Double k) {
		tempK = k;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tempK == null) ? 0 : tempK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Temperature other = (Temperature) obj;
		if (tempK == null) {
			if (other.tempK != null)
				return false;
		} else if (!this.getTemp(Scale.KELVIN).equals(other.getTemp(Scale.KELVIN)))
			return false;
		return true;
	}

	@Override
	public int compareTo(Temperature t) {
		if (this.equals(t)) {
			return 0;
		}
		if (this.getTemp(Scale.KELVIN) > t.getTemp(Scale.KELVIN)) {
			return 1;
		} 
		return -1;
	}

	public static enum Scale {
		KELVIN("K", (Double d) -> d, (Double d) -> d), 
		CELSIUS("C", (Double d) -> d + 273.15, (Double d) -> d - 273.15),
		FAHRENHEIT("F", (Double d) -> (d + 459.67) * 5 / 9,	(Double d) -> d * 9 / 5 - 459.67);

		private String abbv;
		private Function<Double, Double> toKelvin;
		private Function<Double, Double> fromKelvin;

		private Scale(String abbv, Function<Double, Double> toKelvin, Function<Double, Double> fromKelvin) {
			this.abbv = abbv;
			this.toKelvin = toKelvin;
			this.fromKelvin = fromKelvin;
		}

		public String getAbbv() {
			return abbv;
		}

		public Double toKelvin(Double i) {
			return toKelvin.apply(i);
		}

		public Double fromKelvin(Double i) {
			return fromKelvin.apply(i);
		}

	}

}
