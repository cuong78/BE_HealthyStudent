package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramParticipationRequest {
    @NotBlank(message = "Student ID is required")
    @Schema(example = "STU001")
    String studentID;

    @NotBlank(message = "Program ID is required")
    @Schema(example = "PRG001")
    String programID;
}
