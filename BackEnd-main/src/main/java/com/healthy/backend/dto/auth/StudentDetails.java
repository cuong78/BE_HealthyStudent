package com.healthy.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetails {

    @Schema(example = "10")
    @NotNull(message = "Grade is required")
    private Integer grade;

    @NotBlank(message = "Class name is required")
    @Schema(example = "A")
    private String className;

    @NotBlank(message = "School name is required")
    @Schema(example = "Example School")
    private String schoolName;
}
