package com.stephen.spring_boot_api.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    String name;

    String description;

    // 1 role has many permissions, 1 permission thuoc nhieu roles
    @ManyToMany
    Set<Permission> permissions;
}
