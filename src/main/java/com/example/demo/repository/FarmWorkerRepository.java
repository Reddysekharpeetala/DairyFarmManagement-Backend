package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FarmWorker;

public interface FarmWorkerRepository extends JpaRepository<FarmWorker, Long> {

}