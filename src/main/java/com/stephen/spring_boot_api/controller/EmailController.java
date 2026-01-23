package com.stephen.spring_boot_api.controller;

import org.springframework.web.bind.annotation.PostMapping;

import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.entity.Email;
import com.stephen.spring_boot_api.service.EmailSenderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class EmailController {
    private EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send")
    public ApiResponse<String> sendEmail(@RequestBody Email email) {
        return emailSenderService.sendEmail(email);
    }
}
