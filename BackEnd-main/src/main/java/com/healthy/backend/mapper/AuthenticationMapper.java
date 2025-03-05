package com.healthy.backend.mapper;

import com.healthy.backend.dto.auth.response.AuthenticationResponse;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse toAuthenticationResponse(
            Users user, Psychologists psychologist,
            String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .psychologistId(psychologist.getPsychologistID())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .build();
    }
        public AuthenticationResponse toAuthenticationResponse(
            Users user,
            String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .build();
    }
    public AuthenticationResponse toAuthenticationResponse(
            Users user, Students student,
            String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .studentId(student.getStudentID())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .build();
    }
}
