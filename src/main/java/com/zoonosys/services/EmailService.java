package com.zoonosys.services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${mail-from.address:noreply@zoonosys.com}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendBaseUrl;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendPasswordResetEmail(String toEmail, String username, String resetToken){
        String resetUrl = "http://localhost:8080/reset-password?token=" + resetToken;

        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Recuperação de Senha - ZoonoSys");

            Context context = new Context(new Locale("pt", "BR"));
            context.setVariable("userName", username);
            context.setVariable("resetUrl", resetUrl);
            context.setVariable("token", resetToken);

            String htmlContent = templateEngine.process("reset-password-email", context);

            helper.setText(htmlContent, true);

            emailSender.send(message);

        } catch (MessagingException e){
            logger.error("Erro ao enviar e-mail de reset de senha para {}: {}", toEmail, e.getMessage(), e);
        }
    }
}
