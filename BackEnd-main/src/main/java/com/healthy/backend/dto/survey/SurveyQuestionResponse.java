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
    @Schema(example = "Q001")
    private String questionId;
    @Schema(example = "How are you feeling today?")
    private String questionText;
    @Schema(example = "General")
    private String questionCategory;
    @Schema(examples = {"1", "2", "3", "4", "5"})
    private List<String> questionOptions;
}
