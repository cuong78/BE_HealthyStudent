package com.healthy.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NotBlank
public class ErrorResponse {

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "Invalid request")
    private String message;

    @Schema(example = "2023-01-01T00:00:00")
    private LocalDateTime timestamp;
}
