package com.stephen.spring_boot_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephen.spring_boot_api.dto.ApiResponse;

@RestController
@RequestMapping("/nestjs")
public class NestjsMicroController {
    private final String NESTJS_SERVICE_URL = "http://localhost:3000";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/health-check")
    public ApiResponse<Object> healthCheck() {
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(NESTJS_SERVICE_URL + "/health-check", String.class);

            ApiResponse<Object> apiResponse = new ApiResponse<>();

            // Parse JSON and extract nested field using Jackson
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode dataNode = jsonNode.path("data");
            apiResponse.setData(dataNode);
            apiResponse.setMessage(("NestJS service is healthy"));
            return apiResponse;
        } catch (Exception e) {
            ApiResponse<Object> apiResponse = new ApiResponse<>();
            apiResponse.setData("Error connecting to NestJS service: " + e.getMessage());
            return apiResponse;
        }
    }
}
