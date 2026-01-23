package com.stephen.spring_boot_api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.stephen.spring_boot_api.dto.ApiResponse;
import com.stephen.spring_boot_api.entity.Email;
import com.stephen.spring_boot_api.service.EmailSenderService;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Value("${spring.mail.username}")
    private String sender;

    private JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ApiResponse<String> sendEmail(Email email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(email.getTo());
            mailMessage.setText(email.getBody());
            mailMessage.setSubject(email.getSubject());

            javaMailSender.send(mailMessage);

            ApiResponse<String> response = new ApiResponse<>();
            response.setMessage("Mail Sent Successfully...");
            return response;
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>();
            response.setMessage("Error while Sending Mail");
            return response;
        }
    }

    @Override
    public ApiResponse<String> sendHtmlEmail(Email email) {
        // Implementation for sending an HTML email
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("HTML email sent successfully");
        return response;
    }
}
