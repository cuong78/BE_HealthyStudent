package com.healthy.backend.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckOutRequest {
    @Schema(example = "")
    private String psychologistNote;
}