package com.stephen.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stephen.spring_boot_api.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, String> {}
