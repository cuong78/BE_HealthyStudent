package com.healthy.backend.dto.survey;

import com.healthy.backend.entity.Surveys;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SurveysResponse {
    private String surveyId;
    private String surveyName;
    private String description;
    private String category;
    private Surveys.SurveyType surveyType;
    private LocalDateTime createdAt;
}