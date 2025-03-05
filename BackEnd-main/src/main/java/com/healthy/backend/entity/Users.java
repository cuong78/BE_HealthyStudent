package com.healthy.backend.entity;

import com.healthy.backend.enums.Gender;
import com.healthy.backend.enums.Role;
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

    @Column(name = "HashedID", nullable = false)
    private String hashedID;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "FullName", length = 100, nullable = false)
    private String fullName;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "PhoneNumber", length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender")
    private Gender gender;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "IsVerified", nullable = false)
    private boolean isVerified;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @Column(name = "VerificationToken", nullable = false)
    private String verificationToken;

    @Column(name = "TokenExpiration", nullable = false)
    private LocalDateTime tokenExpiration;

    @PrePersist
    protected void onCreate() {
        isVerified = false;
        isDeleted = false;
        isActive = true;
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
        return hashedID;
    }

    public boolean isPresent() {
        return userId != null;
    }
}