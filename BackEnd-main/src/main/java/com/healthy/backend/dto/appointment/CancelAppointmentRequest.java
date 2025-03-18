package com.healthy.backend.dto.appointment;

import lombok.Data;

@Data
public class CancelAppointmentRequest {
    private String reason;
}