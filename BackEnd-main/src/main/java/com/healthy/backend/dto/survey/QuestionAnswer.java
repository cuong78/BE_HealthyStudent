package com.healthy.backend.dto.survey;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionAnswer {
    @NotNull
    private String questionId;

    @NotNull
    private String optionId;
}