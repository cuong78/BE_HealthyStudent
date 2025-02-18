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
public class SurveysResponse {
    @Schema(example = "SUR001")
    private String surveyID;
    @Schema(example = "Example Survey")
    private String surveyName;
    @Schema (example = "Survey Description")
    private String description;
    @Schema (example = "General")
    private String categoryID;
    @Schema (example = "Finished")
    private String status;
    @Schema (example = "2023-01-01")
    private String createdAt;
    @Schema (example = "US001")
    private String createBy;
}
