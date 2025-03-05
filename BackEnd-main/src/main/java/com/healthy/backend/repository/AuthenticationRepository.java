package com.healthy.backend.repository;

import com.healthy.backend.entity.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Users, String> {
    Users findByHashedID(
            @NotBlank(message = "Username is required")
            String username);

    Users findByEmail(
            @NotBlank(message = "Email is required")
            String email);

    Users findByVerificationToken(
            @NotBlank(message = "Token is required")
            String token);

    Users findByPhoneNumber(
            @NotBlank(message = "Phone number is required")
            String phoneNumber);

    boolean existsByHashedID(
            @NotBlank(message = "Username is required")
            String username);

    boolean existsByEmail(
            @NotBlank(message = "Email is required")
            String email);

    boolean existsByPhoneNumber(
            @NotBlank(message = "Phone number is required")
            String phoneNumber);
}

