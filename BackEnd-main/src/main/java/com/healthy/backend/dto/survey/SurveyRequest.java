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
public class SurveyRequest {
    @Schema(example = "Example Survey")
    private String title;
    @Schema (example = "Survey Description")
    private String description;
    @Schema (example = "Details")
    private String detailedDescription;
    @Schema (example = "General")
    private String categoryID;
    @Schema (example = "15-20")
    private String duration;
    @Schema (example = "10")
    private Integer numberOfQuestions;
    @Schema (example = "Anxiety")
    private String categoryName;
    @Schema (example = "Finished")
    private String status;
    @Schema (example = "2023-01-01")
    private String createdAt;
    @Schema (example = "US001")
    private String createBy;
    @Schema(example = "")
    private List<SurveyQuestionRequest> question;
}
