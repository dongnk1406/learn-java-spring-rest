package com.stephen.spring_boot_api.service;

import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.entity.Email;

public interface EmailSenderService {

    ApiResponse<String> sendEmail(Email email);

    ApiResponse<String> sendHtmlEmail(Email email);
}
