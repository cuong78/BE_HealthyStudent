package com.healthy.backend.dto.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentReportRequest {
    @Schema(example = "title")
    private String title;
    @Schema(example = "1")
    private String reason;
    @Schema(example = "Good")
    private String comment;
    @Schema(example = "APP001")
    private String appointmentId;
    @Schema(example = "PSY001")
    private String psychologistId;
    @Schema(example = "S001")
    private String studentId;
    @Schema(example = "0")
    private Integer depressionScore;
    @Schema(example = "0")
    private Integer anxietyScore;
    @Schema(example = "0")
    private Integer stressScore;
    @Schema(example = "10")
    private Integer totalScore;
    @Schema(example = "")
    private String status;
}
