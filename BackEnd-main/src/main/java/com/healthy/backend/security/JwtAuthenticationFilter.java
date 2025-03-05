package com.healthy.backend.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Extract claims using the extractClaim method
                String email = jwtService.extractClaim(token, claims -> claims.get("email", String.class));
                String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
                boolean isVerified = jwtService.extractClaim(token, claims -> claims.get("isVerified", Boolean.class));
                boolean isActive = jwtService.extractClaim(token, claims -> claims.get("isActive", Boolean.class));
                String hashedID = jwtService.extractClaim(token, Claims::getSubject); // Extract subject directly

                // Validation of the claims
                if (hashedID == null || email == null || role == null) {
                    returnErrorResponse(response, "Invalid JWT token: Missing essential claims." );
                    return;
                }

                if (!isVerified) {
                    returnErrorResponse(response, "User is not verified.");
                    return;
                }

                if (!isActive) {
                    returnErrorResponse(response, "User is not active." );
                    return;
                }

                // Convert the role to GrantedAuthority
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // Create the Authentication object
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

                // Set the authentication to the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                returnErrorResponse(response, "Unauthorized: Invalid or expired token.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    // Helper method to return an error response
    private void returnErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        String jsonResponse = "{\"error\": \"" + message + "\"}";
        response.getWriter().write(jsonResponse);
    }
} 