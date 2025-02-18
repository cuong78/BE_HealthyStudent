package com.healthy.backend.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyQuestionResultResponse {
    @Schema(example = "Q001")
    private String questionId;
    @Schema(example = "Anxiety")
    private String categoryName;
    @Schema(example = "How often do you have panic attacks?")
    private String questionText;
    @Schema(example = "SUR002")
    private String resultId;
    @Schema(example = "A001")
    private String answerId;
    @Schema(example = "Good")
    private String answer;
    @Schema(example = "1")
    private Integer score;
} 