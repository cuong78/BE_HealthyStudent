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
public class SurveyQuestionResponse {
    @Schema(example = "SUR001")
    private String surveyId;
    @Schema(example = "How are you feeling today?")
    private String title;
    @Schema(example = "")
    private String description;
    @Schema(example = "")
    private Integer numberOfQuestions;
    @Schema(example = "")
    private List<QuestionResponse> questionList;
    @Schema(example = "")
    private String completeStatus;
    @Schema(example = "")
    private String totalScore;
}
