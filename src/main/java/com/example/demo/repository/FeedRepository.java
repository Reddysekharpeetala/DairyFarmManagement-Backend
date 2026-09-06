package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}