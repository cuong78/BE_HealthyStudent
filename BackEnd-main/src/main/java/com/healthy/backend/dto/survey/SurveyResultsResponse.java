package com.healthy.backend.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResultsResponse {
    @Schema(example = "SUR001")
    private String surveyId;
    @Schema(example = "Example Survey")
    private String surveyName;
    @Schema(example = "Survey Description")
    private String description;
    @Schema(examples = {"Question 1", "Question 2", "Question 3", "Question 4", "Question 5"})
    private List<SurveyQuestionResultResponse> questions;
    @Schema(example = "")
    private String studentId;
    @Schema(example = "")
    private List<StatusStudent> std;
    @Schema(example = "")
    private String status;
    @Schema(example = "")
    private List<StatusStudent> totalScore;

}
