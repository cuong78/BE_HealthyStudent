package com.healthy.backend.dto.survey;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionDTO {
    private String questionId;
    private String questionText;
    private String questionGroup;
    private List<OptionDTO> options;
}
