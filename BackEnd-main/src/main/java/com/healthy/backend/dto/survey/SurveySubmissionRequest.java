package com.healthy.backend.dto.survey;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SurveySubmissionRequest {
    @NotNull
    private String studentId;

    @NotNull
    @Valid
    private List<QuestionAnswer> answers;
}

