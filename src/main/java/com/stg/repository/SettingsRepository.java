package com.stg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.model.Settings;

@Deprecated
public interface SettingsRepository  extends JpaRepository<Settings, Long>  {

}
