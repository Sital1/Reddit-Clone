package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.service.MailContentBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilderImpl implements MailContentBuilder {

    private final TemplateEngine templateEngine;

    /**
     * Injects the required message to the mail Template
     * @param message The email message
     * @return The string containg the result of evaluating the template.
     */
    @Override
    public String build(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }

}
