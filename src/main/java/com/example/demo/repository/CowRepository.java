package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Cow;


public interface CowRepository extends JpaRepository<Cow, Long> {

}