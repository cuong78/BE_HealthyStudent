package com.healthy.backend.security;

import com.healthy.backend.entity.Parents;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.InvalidTokenException;
import com.healthy.backend.repository.ParentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PsychologistRepository psychologistRepository;
    private final ParentRepository parentRepository;

    public String validateRequestUserID(HttpServletRequest request, String userId) {
        Users user = retrieveUser(request);
        return (userId == null || !userRepository.existsById(userId)) ? user.getUserId() : userId;
    }

    public String validateRequestStudentID(HttpServletRequest request, String studentId) {
        Users user = retrieveUser(request);
        Students student = studentRepository.findByUserID(user.getUserId());
        return (studentId == null || !studentRepository.existsById(studentId)) ? student.getStudentID() : studentId;
    }

    public boolean isManager(HttpServletRequest request) {
        return validateRole(request, Role.MANAGER);
    }

    public boolean isStudent(HttpServletRequest request) {
        return validateRole(request, Role.STUDENT);
    }

    public boolean isPsychologist(HttpServletRequest request) {
        return validateRole(request, Role.PSYCHOLOGIST);
    }

    public boolean isParent(HttpServletRequest request) {
        return validateRole(request, Role.PARENT);
    }

    public boolean validateRoles(HttpServletRequest request, List<Role> role) {
        return role.stream().anyMatch(r -> validateRole(request, r));
    }

    private boolean _isManager(HttpServletRequest request) {
        HashMap<String, ?> map = extractRequest(request);
        String role = (String) map.get("role");
        return role.equals("ROLE_MANAGER");
    }

    public Users retrieveUser(HttpServletRequest request) {
        HashMap<String, ?> map = extractRequest(request);
        String uid = (String) map.get("uid");
        return userRepository.findById(uid)
                .orElseThrow(() -> new InvalidTokenException("User not found for given token"));
    }

    public boolean validate(HttpServletRequest request, Users user) {
        HashMap<String, ?> map = extractRequest(request);

        if (!(boolean) map.get("isActive")) {
            throw new InvalidTokenException("Your account is inactive.");
        }
        if (!(boolean) map.get("isVerified")) {
            throw new InvalidTokenException("Your account is not verified.");
        }
        if (!map.get("hashedID").equals(user.getHashedID())) {
            throw new InvalidTokenException("Invalid token: hashedID does not match.");
        }
        if (!map.get("email").equals(user.getEmail())) {
            throw new InvalidTokenException("Invalid token: email does not match.");
        }
        if (!map.get("uid").equals(user.getUserId())) {
            throw new InvalidTokenException("Invalid token: uid does not match.");
        }
        return true;
    }

    public String getRoleID(Users user) {
        return switch (user.getRole()) {
            case PARENT -> Optional.ofNullable(parentRepository.findByUserID(user.getUserId()))
                    .map(Parents::getParentID)
                    .orElse("");
            case STUDENT -> Optional.ofNullable(studentRepository.findByUserID(user.getUserId()))
                    .map(Students::getStudentID)
                    .orElse("");
            case PSYCHOLOGIST -> Optional.ofNullable(psychologistRepository.findByUserID(user.getUserId()))
                    .map(Psychologists::getPsychologistID)
                    .orElse("");
            default -> "";
        };
    }

    public boolean validateIsActive(HttpServletRequest request) {
        HashMap<String, ?> map = extractRequest(request);
        return (boolean) map.get("isActive");
    }

    public boolean validateHashedID(HttpServletRequest request, String hashedID) {
        HashMap<String, ?> map = extractRequest(request);
        return hashedID != null && hashedID.equals(map.get("hashedID"));
    }

    public boolean validateIsVerified(HttpServletRequest request) {
        HashMap<String, ?> map = extractRequest(request);
        return (boolean) map.get("isVerified");
    }

    public boolean validateRole(HttpServletRequest request, List<Role> roles) {
        HashMap<String, ?> map = extractRequest(request);
        String userRole = (String) map.get("role");
        if (userRole != null && userRole.startsWith("ROLE_")) {
            userRole = userRole.substring(5); // Remove "ROLE_"
        }
        return roles.contains(Role.valueOf(userRole));
    }

    public boolean validateRole(HttpServletRequest request, Role role) {
        return validateRole(request, List.of(role));
    }

    public boolean validateEmail(HttpServletRequest request, String email) {
        HashMap<String, ?> map = extractRequest(request);
        String userEmail = (String) map.get("email");
        return userEmail.equals(email);
    }

    public boolean validateUID(HttpServletRequest request, String uid) {
        HashMap<String, ?> map = extractRequest(request);
        String userUID = (String) map.get("uid");
        return !userUID.equals(uid);
    }

    public boolean validateUID(HttpServletRequest request) {
        HashMap<String, ?> map = extractRequest(request);
        String userUID = (String) map.get("uid");
        return userUID != null;
    }

    private HashMap<String, ?> extractRequest(HttpServletRequest request) {

        String authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseGet(() -> request.getHeader(HttpHeaders.WWW_AUTHENTICATE));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token: missing or malformed.");
        }

        String token = authHeader.substring(7);
        validateToken(token);

        return new HashMap<>() {{
            put("hashedID", Optional.ofNullable(jwtService.extractHashedID(token))
                    .orElseThrow(() -> new InvalidTokenException("Invalid token: missing hashedID")));
            put("role", Optional.ofNullable(jwtService.extractRole(token))
                    .orElseThrow(() -> new InvalidTokenException("Invalid token: missing role")));
            put("email", Optional.ofNullable(jwtService.extractEmail(token))
                    .orElseThrow(() -> new InvalidTokenException("Invalid token: missing email")));
            put("uid", Optional.ofNullable(jwtService.extractUserId(token))
                    .orElseThrow(() -> new InvalidTokenException("Invalid token: missing userId")));
            put("isVerified", jwtService.extractIsVerified(token));
            put("isActive", jwtService.extractIsActive(token));
        }};
    }

    private HashMap<String, ?> _extractRequest(HttpServletRequest request) {

        String authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseGet(() -> request.getHeader(HttpHeaders.WWW_AUTHENTICATE));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token: missing or malformed.");
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired token.");
        }

        String hashedID = jwtService.extractHashedID(token);
        String role = jwtService.extractRole(token);
        String email = jwtService.extractEmail(token);
        String userId = jwtService.extractUserId(token);
        boolean isVerified = jwtService.extractIsVerified(token);
        boolean isActive = jwtService.extractIsActive(token);

        return new HashMap<>() {{
            put("hashedID", hashedID);
            put("role", role);
            put("email", email);
            put("uid", userId);
            put("isVerified", isVerified);
            put("isActive", isActive);
        }};
    }

    private void validateToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new InvalidTokenException("Invalid or expired token.");
        }
    }

    private void validateToken(String token, Users user) {
        if (!jwtService.isTokenValid(token, user)) {
            throw new InvalidTokenException("Invalid or expired token.");
        }
    }
}