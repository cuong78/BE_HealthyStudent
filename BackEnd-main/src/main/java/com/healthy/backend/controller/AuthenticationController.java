package com.healthy.backend.controller;

import com.healthy.backend.dto.auth.AuthenticationRequest;
import com.healthy.backend.dto.auth.AuthenticationResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.dto.auth.VerificationResponse;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.LogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutHandler;

    @Operation(
            summary = "Register new user",
            description = "Register a new user with the provided details"
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticate a user and return JWT tokens"
    )
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @Operation(
            summary = "Refresh token",
            description = "Get a new access token using refresh token"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @Operation(
            deprecated = true,
            summary = "Initiate password reset",
            description = "Send password reset email to user"
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {
        authenticationService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent if account exists");
    }

    @Operation(
            deprecated = true,
            summary = "Reset password",
            description = "Reset password using token from email"
    )
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam @Size(min = 8, message = "Password must be at least 8 characters long")
            @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                    message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
            String newPassword
    ) {
        try {
            authenticationService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password successfully reset");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Logout user",
            description = "Logout the current user"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.ok("Successfully logged out");
    }

    @GetMapping("/verify")
    public ResponseEntity<VerificationResponse> verify(@RequestParam String token) {
        if (token == null) {
            throw new RuntimeException("This token is invalid");
        }
        if (!authenticationService.isVerificationTokenValid(token)) {
            throw new ResourceAlreadyExistsException("This token is invalid");
        }
        if (!authenticationService.isVerificationTokenExpired(token)) {
            throw new RuntimeException("This token is expired");
        }

        boolean isVerified = authenticationService.verifyUser(token).isVerified();

        if (isVerified) {
            // Redirecting to login page or success page
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Content-Type", "text/html")
                    .header("Location", "http://localhost:8080/redirect.html")
                    .build();
        }
        throw new RuntimeException("This token is invalid");
    }
}