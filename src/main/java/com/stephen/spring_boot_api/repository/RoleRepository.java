package com.stephen.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stephen.spring_boot_api.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
