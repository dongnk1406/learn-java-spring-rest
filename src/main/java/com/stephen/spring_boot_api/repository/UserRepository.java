package com.stephen.spring_boot_api.repository;

import com.stephen.spring_boot_api.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String userName);
    Optional<User> findByUsername(String username);
}
