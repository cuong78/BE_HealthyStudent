package com.healthy.backend.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {

    @Schema(example = "STU001")
    private String studentId;
    @Schema(example = "UID001")
    private String userId;
    @Schema(example = "10")
    private Integer grade;
    @Schema(example = "A")
    private String className;
    @Schema(example = "Example School")
    private String schoolName;
    @Schema(example = "Student Name")
    private String fullName;
    @Schema(example = "student@example.com")
    private String email;
    @Schema(example = "1234567890")
    private String phone;
    @Schema(example = "Example Address")
    private String address;
    @Schema(example = "Male")
    private String gender;
    @Schema(example = "0")
    private Integer depressionScore;
    @Schema(example = "0")
    private Integer anxietyScore;
    @Schema(example = "0")
    private Integer stressScore;
    @Schema(examples = {"General", "Behavior", "Academic", "Emotional"} )
    private List<SurveyResultsResponse> surveyResults;
    @Schema(example = "2023-01-01")
    private LocalDateTime createdAt;
    @Schema(example = "2023-01-02")
    private LocalDateTime updatedAt;
    @Schema(example = "UsersResponse")
    private UsersResponse info;
    @Schema(example = "")
    private String programStatus;
    @Schema(example = "")
    private UsersResponse usersResponse;

}