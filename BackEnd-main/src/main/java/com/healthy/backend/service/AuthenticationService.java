package com.healthy.backend.service;

import com.healthy.backend.dto.auth.AuthenticationRequest;
import com.healthy.backend.dto.auth.AuthenticationResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.dto.auth.VerificationResponse;
import com.healthy.backend.entity.RefreshToken;
import com.healthy.backend.entity.Users;
import com.healthy.backend.mapper.UserMapper;
import com.healthy.backend.repository.AuthenticationRepository;
import com.healthy.backend.repository.RefreshTokenRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserMapper usermapper;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenDuration;

    @Value("${app.url}")
    private String siteURL;

    // Register new user
    public AuthenticationResponse register(RegisterRequest request) {

        if (authenticationRepository.findByPhoneNumber(request.getPhoneNumber()) != null) {
            throw new RuntimeException("This phone number is already in use for another account");
        }

        if (authenticationRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("This username is already taken");
        }

        if (authenticationRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("This email is already in use for another account");
        }

        String token = jwtService.generateVerificationToken(request.getEmail());
        String password = passwordEncoder.encode(request.getPassword());
        Users savedUser = userRepository.save(
                usermapper.buildUserEntity(request, token, getUserLastCode(), password));

        // Send verification email
        String verificationUrl = UriComponentsBuilder.fromUriString(siteURL)
                .path("/api/auth/verify")
                .queryParam("token", token)
                .toUriString();

        if (savedUser.getEmail().contains("example")) {
            return AuthenticationResponse.builder()
                    .userId(savedUser.getUserId())
                    .role(savedUser.getRole().toString())
                    .build();
        }
        emailService.sendVerificationEmail(request.getEmail(), "Click the link to verify your email: " + verificationUrl,
                "Verify Your Account");

        return AuthenticationResponse.builder()
                .userId(savedUser.getUserId())
                .role(savedUser.getRole().toString())
                .build();
    }

    // Authentication
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authToken;
        Users user;
        String username;

        if (request.getLoginIdentifier().contains("@")) {
            user = authenticationRepository.findByEmail(request.getLoginIdentifier());
        } else {
            user = authenticationRepository.findByUsername(request.getLoginIdentifier());
        }
        // Generate authToken
        assert user != null;
        username = user.getUsername();
        authToken = new UsernamePasswordAuthenticationToken(
                username,
                request.getPassword()
        );

        authenticationManager.authenticate(authToken);

        // Generate tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token to database
        saveRefreshToken(user.getUserId(), refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    // Refresh token
    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) {
        final String authHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION) == null
                        ? (request.getHeader(HttpHeaders.WWW_AUTHENTICATE) == null
                        ? null : request.getHeader(HttpHeaders.WWW_AUTHENTICATE))
                        : request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid refresh token");
        }

        final String refreshToken = authHeader.substring(7);
        final String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        Users user = authenticationRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Verify refresh token exists in database
        RefreshToken storedToken = refreshTokenRepository.findByHashedToken(hashToken(refreshToken));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expired");
        }

        // Generate new tokens
        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Update stored refresh token
        saveRefreshToken(user.getUserId(), newRefreshToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getUserId())
                .role(user.getRole().toString())
                .build();
    }

    // Initiate password reset
    public void initiatePasswordReset(String email) {
        Users user = authenticationRepository.findByEmail(email);
        if (user == null) {
            // Don't reveal if email exists or not
            return;
        }

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        authenticationRepository.save(user);

        // Generate password reset link
        String resetLink = UriComponentsBuilder.fromUriString(siteURL)
                .path("/api/auth/reset-password")
                .queryParam("token", resetToken)
                .toUriString();

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    // Reset password
    public void resetPassword(String token, String newPassword) {
        Users user = authenticationRepository.findByResetToken(token);
        if (user == null || !user.isResetTokenValid(token)) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        // Update password and clear reset token
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.clearResetToken();

        // Save user
        authenticationRepository.save(user);

        // Optionally invalidate all refresh tokens for this user
        refreshTokenRepository.deleteByUserId(user.getUserId());
    }

    // Verify email
    public VerificationResponse verifyUser(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        Users user = userRepository.findByEmail(jwtService.extractEmail(token));

        user.setVerified(true);
        userRepository.save(user);

        return new VerificationResponse("Email verified successfully. You can now log in.", token, true);
    }

    // Check if verification token is valid
    public boolean isVerificationTokenValid(String token) {
        return authenticationRepository.findByVerificationToken(token) != null;
    }

    // Check if verification token is expired
    public boolean isVerificationTokenExpired(String token) {
        return authenticationRepository.findByVerificationToken(token).getTokenExpiration().isAfter(LocalDateTime.now());
    }

    // Save refresh token to database
    private void saveRefreshToken(String userId, String refreshToken) {

        // Check if refresh token already exists
        if (!refreshTokenRepository.findByUserId(userId).isEmpty()) {
            // Delete any existing refresh tokens for this user if they exist
            refreshTokenRepository.deleteByUserId(userId);
        }

        // Create new refresh token
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setHashedToken(hashToken(refreshToken));
        token.setExpiresAt(LocalDateTime.now().plus(Duration.ofMillis(refreshTokenDuration)));

        refreshTokenRepository.save(token);
    }

    // Use a secure hashing algorithm
    private String hashToken(String token) {
        return passwordEncoder.encode(token);
    }


    // Generate user ID
    private String getUserLastCode() {
        if (userRepository.findAll().isEmpty()) {
            return "US001";
        }
        String lastCode = userRepository.findLastUserId();
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last participation code");
        }
        String prefix = lastCode.substring(0, 2);
        int number = Integer.parseInt(lastCode.substring(2));
        return prefix + String.format("%03d", number + 1);
    }
}
