package com.stephen.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stephen.spring_boot_api.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, String> {}
