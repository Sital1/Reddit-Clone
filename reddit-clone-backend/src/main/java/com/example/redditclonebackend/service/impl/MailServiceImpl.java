package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.exceptions.SpringRedditException;
import com.example.redditclonebackend.model.NotificationEmail;
import com.example.redditclonebackend.service.MailContentBuilder;
import com.example.redditclonebackend.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    /**
     * Builds the notification and sends the notification to the user. Runs asynchronously in a separate thread to contact the mailtrap server
     * @param notificationEmail The notification email that describes sender, subject, recipient and body of email
     */
    @Override
    @Async
    public void sendEmail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("redditclone@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation Email Sent!");
        }catch (MailException e){
            e.printStackTrace();
            throw  new SpringRedditException("Exception occured when sending mail to "+ notificationEmail.getRecipient());
        }

    }


}
