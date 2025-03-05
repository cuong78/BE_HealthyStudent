package com.healthy.backend.service;

import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.TimeSlots;
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

    private String getEmailBody(String subject, String content, String name) {
        return EmailTemplate.getEmailBody(subject, content, name);
    }

    @Async
    public void sendNewAppointmentEmail(String email, String psychologistName, Students student, String ID, TimeSlots timeSlots ,String subject) {
        String content = subject.toLowerCase().contains("transfer") ? "We are pleased to informed that an appointment of yours has been transferred.\n\n" +
                "The details of the transferred appointment are as follows:\n\n" +
                "**Student:** " + student.getUser().getFullName() + "\n" +
                "**Class:** " + student.getGrade() + student.getClassName() + "\n" +
                "**Appointment ID:** " + ID + "\n" +
                "**Date:** " + timeSlots.getSlotDate() + "\n" +
                "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                "Please log in to the system for further details." :
                // If the subject doesn't contain "transfer", use the original content
                "We are pleased to inform you that a new appointment has been scheduled.\n\n" +
                "You have a new appointment scheduled with the following details:\n" +
                "**Student:** " + student.getUser().getFullName() + "\n" +
                "**Class:**" + student.getGrade() + student.getClassName() + "\n" +
                "**Appointment ID:** " + ID + "\n" +
                "**Date:** " + timeSlots.getSlotDate() + "\n" +
                "**Time:** " + timeSlots.getStartTime() + " - " + timeSlots.getEndTime() + "\n\n" +
                "Please log in to the system to view more details or manage your appointments.\n\n";
        sendEmail(email, subject, content, psychologistName);
    }

    @Async
    public void sendPasswordResetEmail(String email, String resetLink, String name) {
        String subject = "Password Reset Request";
        String content = "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                "Click the link to reset your password: " + resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email.";

        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendVerificationEmail(String email, String verificationLink, String name) {
        String subject = "Email Verification Required";
        String content = "Thank you for registering with **Mental Health Care**. " +
                "To complete your registration, please verify your email address by clicking the link below:\n\n" +
                "Click the link to verify your account: " + verificationLink + "\n\n" +
                "This verification link is valid for the next **12 hours**. If you do not complete the verification within this period, you may need to request a new link.\n\n" +
                "If you did not initiate this request, please ignore this email.";

        sendEmail(email, subject, content, name);
    }

    @Async
    public void sendForgotPasswordEmail(String email, String resetLink, String name) {
        String subject = "Forgot Password Request";
        String content = "We have received a request to reset your account password. To proceed, please click the link below:\n\n" +
                "Click the link to reset your password: " + resetLink + "\n\n" +
                "This link will remain valid for the next **12 hours**. If you do not reset your password within this timeframe, you will need to submit a new request.\n\n" +
                "If you did not initiate this request, please disregard this email.";

        sendEmail(email, subject, content, name);
    }

    private void sendEmail(String toEmail, String subject, String body, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(getEmailBody(subject, body, name));
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}

class EmailTemplate {
    public static String getEmailBody(String subject, String bodyContent, String name) {
        return "Subject: " + subject + "\n\n" +
                "Dear " + name + ",\n\n" +
                bodyContent + "\n\n" +
                "For any questions or assistance, please reach out to our support team.\n\n" +
                "Best regards,\n" +
                "Mental Health Care Support Team";
    }
}
