package com.healthy.backend.service;

import com.healthy.backend.entity.Users;
import com.healthy.backend.repository.RefreshTokenRepository;
import com.healthy.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractHashedID(token);

        Users user = authentication != null && authentication.getPrincipal() instanceof Users
                ? (Users) authentication.getPrincipal()
                : null;


        // Check if token is valid
        if (user == null || !jwtService.isTokenValid(token, user)) {
            return;
        }

        // Delete refresh token
        refreshTokenRepository.deleteByUserId(userId);

        // Invalidate the token
        jwtService.invalidateToken(token);

        // Clear session if it exists
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }

        try {
            response.setHeader("Clear-Site-Data", "\"auth\""); // Clear authentication data
            response.setHeader("Location", "/swagger-ui.html"); // Redirect to Swagger UI
            response.setStatus(HttpServletResponse.SC_FOUND); // Set response status to 302
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
        } catch (Exception e) {
            throw new RuntimeException("Error during logout", e);
        }
    }
}