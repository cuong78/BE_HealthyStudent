package com.healthy.backend.security;

import com.healthy.backend.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractHashedID(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public boolean extractIsVerified(String token) {
        return extractClaim(token, claims -> claims.get("isVerified", Boolean.class));
    }

    public boolean extractIsActive(String token) {
        return extractClaim(token, claims -> claims.get("isActive", Boolean.class));
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("uid", String.class));
    }

    public String extractVerificationEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String generateRefreshToken(Users user) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getUserId());
        claims.put("email", user.getEmail());
        claims.put("role", "ROLE_" + user.getRole().name()); // Ensure correct role format
        claims.put("isVerified", user.isVerified());
        claims.put("isActive", user.isActive());
        return generateToken(claims, user);
    }

    // Generate token
    private String generateToken(Map<String, Object> extraClaims, Users user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    // Generate verification token
    public String generateVerificationToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))  // 24 hours expiry
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildToken(Map<String, Object> extraClaims, Users user, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, Users user) {
        final String hashedID = extractHashedID(token);
        return (hashedID.equals(user.getHashedID())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // DO NOT TOUCH
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // For testing
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String hashedID = claims.getSubject();
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        boolean isVerified = claims.get("isVerified", Boolean.class);
        boolean isActive = claims.get("isActive", Boolean.class);

        // Convert role string to GrantedAuthority
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }

    public boolean isTokenRevoked(String token, Set<String> revokedTokens) {
        return revokedTokens.contains(token);
    }

    public void invalidateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            claims.setExpiration(new Date(System.currentTimeMillis() - 1000));
        } catch (Exception e) {
            throw new RuntimeException(e);}
    }
}
