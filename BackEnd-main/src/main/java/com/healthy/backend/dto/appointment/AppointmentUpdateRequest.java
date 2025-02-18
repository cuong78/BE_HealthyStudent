package com.healthy.backend.dto.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentUpdateRequest {

    @Schema(example = "TSPSY001160225")
    private String timeSlotId;
    @Schema(example = "Scheduled")
    private String status;
    @Schema(example = "Notes")
    private String notes;
}
