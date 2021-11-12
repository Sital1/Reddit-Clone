package com.example.redditclonebackend.service;

import com.example.redditclonebackend.exceptions.SpringRedditException;
import com.example.redditclonebackend.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
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
