package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.HealthRecord;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

}