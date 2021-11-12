package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.model.NotificationEmail;
import com.example.redditclonebackend.service.MailContentBuilder;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    /**
     * Builds the notification and sends the notification to the user
     * @param notificationEmail The notification email that describes sender, subject, recipient and body of email
     */
    public void sendEmail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("redditclone@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            
        }

    }


}
