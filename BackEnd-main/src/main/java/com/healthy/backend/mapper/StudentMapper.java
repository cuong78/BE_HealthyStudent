package com.healthy.backend.mapper;

import com.healthy.backend.dto.auth.request.StudentRegisterRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Users;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StudentMapper {

    public StudentResponse buildStudentResponse(
            Students student,
            UsersResponse usersResponse) {
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .info(usersResponse)
                .build();
    }
    public StudentResponse buildStudentResponse(
            Students student,
            List<SurveyResultsResponse> surveyResultsResponseList) {
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .surveyResults(surveyResultsResponseList.isEmpty() ? null : surveyResultsResponseList)
                .build();
    }

    public StudentResponse buildBasicStudentResponse(
            Students student) {
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phone(student.getUser().getPhoneNumber())
                .address(student.getUser().getAddress())
                .gender(student.getUser().getGender().toString())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .build();
    }

    public StudentResponse buildStudentResponse(
            Students student) {
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phone(student.getUser().getPhoneNumber())
                .address(student.getUser().getAddress())
                .gender(student.getUser().getGender().toString())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .build();
    }

    public Students buildStudentEntity(StudentRegisterRequest student, Users user, String studentID) {
        return Students.builder()
                .userID(user.getUserId())
                .studentID(studentID)
                .grade(student.getStudentDetails().getGrade())
                .className(student.getStudentDetails().getClassName())
                .schoolName(student.getStudentDetails().getSchoolName())
                .depressionScore(BigDecimal.valueOf(0.0))
                .anxietyScore(BigDecimal.valueOf(0.0))
                .stressScore(BigDecimal.valueOf(0.0))
                .user(user)
                .build();
    }
}
