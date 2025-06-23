package com.stephen.spring_boot_api.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private List<String> roles;
}
