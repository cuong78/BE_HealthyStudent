package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PsychologistsMapper {

    public PsychologistResponse buildPsychologistResponse(Psychologists psychologist) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .departmentName(psychologist.getDepartment().getName())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .build();
    }
    public PsychologistResponse buildPsychologistResponse(Psychologists psychologist,
                                        List<Appointments> appointments,
                                        Users users) {
        return
                PsychologistResponse.builder()
                        .psychologistId(psychologist.getPsychologistID())
                        .status(psychologist.getStatus().name())
                        .departmentName(psychologist.getDepartment().getName())
                        .yearsOfExperience(psychologist.getYearsOfExperience())
                        .usersResponse(UsersResponse.builder()
                                .fullName(users.getFullName())
                                .username(users.getUsername())
                                .phoneNumber(users.getPhoneNumber())
                                .email(users.getEmail())
                                .gender(users.getGender().toString())
                                .build())
                        .appointment(
                                appointments.isEmpty()
                                        ? Collections.emptyList() : appointments.stream()
                                        .map(a -> AppointmentResponse.builder()
                                                .appointmentID(a.getAppointmentID())
                                                .CreatedAt(a.getCreatedAt())
                                                .Status(a.getStatus().name())
                                                .studentResponse(
                                                        StudentResponse.builder()
                                                                .studentId(a.getStudentID())
                                                                .grade(a.getStudent().getGrade())
                                                                .className(a.getStudent().getClassName())
                                                                .schoolName(a.getStudent().getSchoolName())
                                                                .depressionScore(a.getStudent().getDepressionScore())
                                                                .anxietyScore(a.getStudent().getAnxietyScore())
                                                                .stressScore(a.getStudent().getStressScore())
                                                                .build()
                                                )
                                                .studentNotes(a.getStudentNote())
                                                .timeSlotID(a.getTimeSlotsID())
                                                .UpdatedAt(a.getUpdatedAt()).build()
                                        )
                                        .collect(Collectors.toList())
                        )
                        .build();
    }
}
