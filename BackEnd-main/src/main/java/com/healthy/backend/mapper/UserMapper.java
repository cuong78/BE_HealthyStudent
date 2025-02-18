package com.healthy.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Users;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserMapper {

    // Convert User entity to UserResponse
    public UsersResponse buildUserResponse(
            Users user,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse,
            List<AppointmentResponse> appointmentResponse,
            List<StudentResponse> children
    ) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .psychologistInfo(psychologistResponse)
                .studentInfo(studentResponse)
                .children(children)
                .appointmentsRecord(appointmentResponse)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Convert User entity to UserResponse
    public Users buildUserEntity(RegisterRequest requestRequest, String token, String userId, String password) {
        return Users.builder()
                .userId(userId)
                .username(requestRequest.getUsername())
                .passwordHash(password)
                .fullName(requestRequest.getFullName())
                .email(requestRequest.getEmail())
                .phoneNumber(requestRequest.getPhoneNumber())
                .role(Users.UserRole.valueOf(requestRequest.getRole()))
                .gender(Users.Gender.valueOf(requestRequest.getGender()))
                .verificationToken(token)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .isVerified(false)
//                // Set the token expiration to 12 hours from now
//                .tokenExpiration(LocalDateTime.now().plusHours(12).plusMinutes(30))
                .build();
    }
}
