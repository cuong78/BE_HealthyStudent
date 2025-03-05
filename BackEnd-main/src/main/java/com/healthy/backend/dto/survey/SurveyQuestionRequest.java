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
public class SurveyQuestionRequest {
    @Schema(example = "")
    private String questionText;
    @Schema(example = "")
    private String categoryID;
    @Schema(example = "")
    private List<QuestionOption> questionOptions;
}
