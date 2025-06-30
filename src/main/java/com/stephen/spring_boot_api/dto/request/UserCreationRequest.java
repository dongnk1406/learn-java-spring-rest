package com.stephen.spring_boot_api.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationRequest {
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String firstName;
    private String lastName;
    // @DobConstraint(min = 16, max = 100)
    private LocalDate dateOfBirth;
    private List<String> roles;
}
