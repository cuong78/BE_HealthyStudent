package com.healthy.backend.dto.survey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionDTO {
    private String optionId;
    private String optionText;
    private Integer score;
}
