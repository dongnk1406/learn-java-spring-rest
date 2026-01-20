package com.stephen.spring_boot_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stephen.spring_boot_api.dto.ApiResponse;

@RestController
@RequestMapping("/nestjs")
public class NestjsMicroController {
    private final String NESTJS_SERVICE_URL = "http://localhost:3000";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/health-check")
    public ApiResponse<Object> healthCheck() {
        try {
            ResponseEntity<Object> response =
                    restTemplate.getForEntity(NESTJS_SERVICE_URL + "/health-check", Object.class);

            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setData(response.getBody());
            return apiResponse;
        } catch (Exception e) {
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setData("Error connecting to NestJS service: " + e.getMessage());
            return apiResponse;
        }
    }
}
