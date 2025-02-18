package com.healthy.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mail;

    private String getResetPasswordMailBody(String resetLink) {
        return "Subject: Password Reset Request\n\n" +
                "Dear User,\n\n" +
                "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email. No further action is required on your part.\n\n" +
                "For any assistance, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }

    private String getVerificationMailBody(String verificationLink) {
        return "Subject: Email Verification Required\n\n" +
                "Dear User,\n\n" +
                "Thank you for registering with **Mental Health Care**. " +
                "To complete your registration, please verify your email address by clicking the link below:\n\n" +
                verificationLink + "\n\n" +
                "This verification link is valid for the next **12 hours**. If you do not complete the verification within this period, you may need to request a new link.\n\n" +
                "If you did not initiate this request, please ignore this email.\n\n" +
                "For any questions or assistance, please reach out to our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }


    @Async
    public void sendVerificationEmail(String email, String verificationLink, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(getVerificationMailBody(verificationLink));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }

    }
    @Async
    public void sendPasswordResetEmail(String email, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText(getResetPasswordMailBody(resetLink));

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
        }
    }
}
