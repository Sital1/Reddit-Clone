package com.example.redditclonebackend.service;

import com.example.redditclonebackend.model.NotificationEmail;

public interface MailService {

    void sendEmail(NotificationEmail notificationEmail);
}
