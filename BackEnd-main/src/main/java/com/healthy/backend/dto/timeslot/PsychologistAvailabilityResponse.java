package com.healthy.backend.dto.timeslot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistAvailabilityResponse {
    @Schema(example = "PSY001")
    private PsychologistResponse psychologist;
    @Schema(example = "2023-01-01")
    private List<TimeSlotResponse> availableTimeSlots;

}
