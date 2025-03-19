package com.healthy.backend.dto.survey;

import com.healthy.backend.entity.Surveys;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SurveyDetailDTO {
    private String surveyId;
    private String surveyName;
    private String description;
    private Surveys.SurveyType surveyType;
    private List<QuestionDTO> questions;
}
