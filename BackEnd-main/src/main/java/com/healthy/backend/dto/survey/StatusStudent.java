package com.healthy.backend.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusStudent {
    @Schema(example = "")
    private String studentId;
    @Schema(example = "")
    private String score;
    @Schema(example = "")
    private String status;
    @Schema(example = "")
    private String resultStd;
    @Schema(example = "")
    private Integer valueOfQuestion;
    @Schema(example = "")
    private String completeStatus;
    @Schema(example = "")
    private String studentComplete;


    
}