package com.SpringCrud.SpringAngular.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(587);

        // ✅ Important: For SendGrid SMTP
        mailSender.setUsername("apikey"); // This must be literally 'apikey'
        mailSender.setPassword("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"); // Replace with your actual SendGrid API key

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
