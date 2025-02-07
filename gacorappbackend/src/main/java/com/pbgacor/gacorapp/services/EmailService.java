package com.pbgacor.gacorapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPendingApprovalEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("PB Gacor Account Registration - Pending Approval");
        message.setText("Thank you for registering. Your account is pending approval by the admin.");
        mailSender.send(message);
        logger.info("Pending approval email sent successfully to: {}", toEmail);
    }

    public void sendAccountActivatedEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("PB Gacor Account Registration - Account Activated");
        message.setText("Your account is activated. You can now proceed to log in.");
        mailSender.send(message);
        logger.info("Account activated email sent successfully to: {}", toEmail);
    }

    public void sendAdminNotificationEmail(String username, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("PB Gacor New User Registration");
        message.setText("A new user has registered on PB Gacor:\n\n" +
                        "Username: " + username + "\n" +
                        "Email: " + email + "\n\n" +
                        "Please review and approve the registration in the admin panel.");
        mailSender.send(message);
        logger.info("Admin notification email sent successfully to: {}", adminEmail);
    }
}
