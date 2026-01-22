package com.stephen.spring_boot_api.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stephen.spring_boot_api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String userName);

    Optional<User> findByUsername(String username);

    // Raw SQL
    @Query(value = "SELECT * FROM users WHERE username = ?1", nativeQuery = true)
    Optional<User> findByUsernameNative(@Param("username") String username);

    // transactional UPDATE, DELETE
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET email = ?1 WHERE username = ?2", nativeQuery = true)
    int updateEmailByUsername(String email, String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE username = ?1", nativeQuery = true)
    int deleteByUsername(String username);
}
