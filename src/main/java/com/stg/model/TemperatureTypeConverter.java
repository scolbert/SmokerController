package com.stg.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TemperatureTypeConverter implements AttributeConverter<Temperature, Double>{

	@Override
	public Double convertToDatabaseColumn(Temperature attribute) {
		return attribute.getTemp();
	}

	@Override
	public Temperature convertToEntityAttribute(Double dbData) {
		Temperature temp = new Temperature();
		temp.setTemp(dbData);
		return temp;
	}

}
