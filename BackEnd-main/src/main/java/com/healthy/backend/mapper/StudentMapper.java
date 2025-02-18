package com.healthy.backend.mapper;

import com.healthy.backend.dto.student.StudentRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Students;
import org.springframework.stereotype.Component;

import java.util.List;
@Component

public class StudentMapper {
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
                .surveyResults(surveyResultsResponseList)
                .build();
    }
    public StudentResponse buildStudentResponse(
            Students student) {
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .build();
    }

    public Students buildStudentEntity(StudentRequest student) {
        return Students.builder()
                .studentID(student.getStudentID())
                .fullName(student.getName())
                .email(student.getEmail())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(0)
                .anxietyScore(0)
                .stressScore(0)
                .build();
    }

//       StudentResponse.builder()
//               .studentId(student.getStudentID())
//            .grade(student.getGrade())
//            .className(student.getClassName())
//            .schoolName(student.getSchoolName())
//            .depressionScore(student.getDepressionScore())
//            .anxietyScore(student.getAnxietyScore())
//            .stressScore(student.getStressScore())
//            .build())
}
