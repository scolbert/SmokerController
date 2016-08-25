package com.stg.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stg.model.SmokeSession;

public interface SmokeSessionRepository extends JpaRepository<SmokeSession, Long> {
	
	List<SmokeSession> findByMeat(String meat);

	List<SmokeSession> findByStartDateBetween(Date beginDate, Date endDate);

	@Query("select distinct(meat) from #{#entityName}")
	List<String> getMeats();
}
