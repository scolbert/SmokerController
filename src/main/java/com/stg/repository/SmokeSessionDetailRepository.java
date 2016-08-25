package com.stg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.model.SmokeSessionDetail;

/**
 * TODO:  Do i really need this?
 * @author andrew
 *
 */
public interface SmokeSessionDetailRepository extends JpaRepository<SmokeSessionDetail, Long> {

	List<SmokeSessionDetail> findAllBySession(Long session);
}
