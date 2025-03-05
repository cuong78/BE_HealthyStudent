package com.healthy.backend.dto.timeslot;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTimeSlotResponse {
    @Schema(example = "")
    private String slotId;
    @Schema(example = "")
    private LocalTime startTime;
    @Schema(example = "")
    private LocalTime endTime;
    @Schema(example = "")
    private String period;
}
