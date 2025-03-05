package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramParticipationResponse {
    @Schema(example = "PRG001")
    private String programID;
    @Schema(example = "Example Program")
    private String programName;
    @Schema(example = "2023-01-01")
    private String description;
    @Schema(example = "2023-01-01")
    private Integer numberParticipants;
    @Schema(example = "2")
    private Integer duration;
    @Schema(example = "2023-01-01")
    private String status;
    @Schema(example = "2023-01-01")
    private LocalDateTime createdAt;
    @Schema(example = "2023-01-01")
    private String facilitatorName;

}
