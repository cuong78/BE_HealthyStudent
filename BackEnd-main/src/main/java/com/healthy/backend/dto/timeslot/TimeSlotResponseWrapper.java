package com.healthy.backend.dto.timeslot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotResponseWrapper {
    @Schema(example = "")
    private List<TimeSlotResponse> timeSlots;
    @Schema(example = "")
    private String message;
}
