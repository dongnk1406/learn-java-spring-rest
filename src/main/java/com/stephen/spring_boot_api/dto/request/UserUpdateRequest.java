package com.stephen.spring_boot_api.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;

import com.stephen.spring_boot_api.validator.DobConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String firstName;
    private String lastName;

    @DobConstraint(min = 16, max = 100)
    private LocalDate dateOfBirth;

    private List<String> roles;
}
