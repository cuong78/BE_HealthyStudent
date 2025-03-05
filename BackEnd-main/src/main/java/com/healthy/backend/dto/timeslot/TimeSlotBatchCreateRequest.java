package com.healthy.backend.dto.timeslot;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotBatchCreateRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "")
    private LocalDate slotDate;

    @NotEmpty
    @Schema(example = "")
    private List<String> defaultSlotIds;
}