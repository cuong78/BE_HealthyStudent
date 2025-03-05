package com.healthy.backend.dto.survey;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOption {
    @Schema(example = "")
    private int value;
    @Schema(example = "")
    private String label;
    @Schema(example = "")
    private String answerID;
    @Schema(example = "", nullable = true)
    private boolean checked;
}
