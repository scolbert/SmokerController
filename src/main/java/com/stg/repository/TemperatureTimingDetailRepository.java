package com.stg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.model.TemperatureTiming;
import com.stg.model.TemperatureTimingDetail;

public interface TemperatureTimingDetailRepository extends JpaRepository<TemperatureTimingDetail, Long>{
	
	public List<TemperatureTimingDetail> findAllByTemperatureTiming(TemperatureTiming tempTiming);

}
