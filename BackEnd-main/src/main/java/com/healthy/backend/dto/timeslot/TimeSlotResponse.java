package com.healthy.backend.dto.timeslot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.enums.TimeslotStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeSlotResponse {
    @Schema(example = "TS00127022500")
    private String timeSlotId;
    @Schema(example = "2023-01-01")
    private LocalDate slotDate;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalTime startTime;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalTime endTime;
    @Schema(example = "Available")
    private TimeslotStatus status;

    private int maxCapacity;
    private int currentBookings;
    private boolean booked;
}
