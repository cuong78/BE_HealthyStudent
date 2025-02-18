package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class Users implements UserDetails {

    @Id
    @Column(name = "UserID", length = 36, nullable = false)
    private String userId;

    @Column(name = "Username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "FullName", length = 100, nullable = false)
    private String fullName;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "PhoneNumber", length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", columnDefinition = "ENUM('Male', 'Female', 'Other')")
    private Gender gender;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "ResetToken")
    private String resetToken;

    @Column(name = "ResetTokenExpiry")
    private LocalDateTime resetTokenExpiry;

    @Column(name = "IsVerified", nullable = false)
    private boolean isVerified;

    @Column(name = "VerificationToken", nullable = false)
    private String verificationToken;

    @Column(name = "TokenExpiration", nullable = false)
    private LocalDateTime tokenExpiration;

    @PrePersist
    protected void onCreate() {
        isVerified = false;
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        this.tokenExpiration = LocalDateTime.now().plusHours(12).plusMinutes(30);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isPresent() {
        return userId != null;
    }

    public void setResetToken(String token) {
        this.resetToken = token;
        this.resetTokenExpiry = LocalDateTime.now().plusHours(24);
    }

    public boolean isResetTokenValid(String token) {
        return resetToken != null &&
                resetToken.equals(token) &&
                resetTokenExpiry != null &&
                resetTokenExpiry.isAfter(LocalDateTime.now());
    }

    public void clearResetToken() {
        this.resetToken = null;
        this.resetTokenExpiry = null;
    }

    public enum UserRole {
        STUDENT,
        PARENT,
        PSYCHOLOGIST,
        MANAGER
    }

    public enum Gender {
        Male,
        Female,
        Other
    }
}