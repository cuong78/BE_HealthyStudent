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
public class AppointmentRequest {

    @Schema(example = "US002")
    private String userId;

    @Schema(example = "TSPSY00127022500")
    private String timeSlotId;

    @Schema(hidden = true, example = "Active")
    private String status;

    @Schema(example = "Notes")
    private String note;
}
