package com.example.redditclonebackend.service;

import com.example.redditclonebackend.model.NotificationEmail;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    @Async
    void sendEmail(NotificationEmail notificationEmail);
}
