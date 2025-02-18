package com.healthy.backend.repository;

import com.healthy.backend.entity.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Users, String> {
    Users findByUsername(
            @NotBlank(message = "Username is required")
            String username);

    Users findByEmail(
            @NotBlank(message = "Email is required")
            String email);

    Users findByResetToken(
            @NotBlank(message = "Token is required")
            String resetToken);

    Users findByVerificationToken(
            @NotBlank(message = "Token is required")
            String token);

    Users findByPhoneNumber(
            @NotBlank(message = "Phone number is required")
            String phoneNumber);
}

