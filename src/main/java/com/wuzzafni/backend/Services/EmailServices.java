package com.wuzzafni.backend.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
@Service
@AllArgsConstructor
public class EmailServices {
    private final JavaMailSender mailSender;

    @Async
    public void sendHtmlEmail(String receiver, String link, String name) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("reikomahmoud208@gmail.com");
        helper.setTo(receiver);
        helper.setSubject("Welcome to Wuzzufni");

        // Read the HTML template
        String emailContent;
        try (var inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/templates/Wuzzufni_Email.html"))) {
            emailContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Replace placeholders
        emailContent = emailContent.replace("{{link}}", link);
        emailContent = emailContent.replace("{{name}}", name);
        helper.setText(emailContent, true);
        helper.addInline("wuzzufni", new ClassPathResource("static/wuzzufni.png"));

        mailSender.send(message);

    }
}
