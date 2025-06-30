package com.stephen.spring_boot_api.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invalidated_tokens")
public class InvalidatedToken {
    @Id
    String id;

    Date expiryTime;
}
