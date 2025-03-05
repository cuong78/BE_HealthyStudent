package com.healthy.backend.dto.manager;

import lombok.Data;

import java.util.Map;

@Data
public class AppointmentStatsResponse {
    private Map<String, Long> appointmentCounts; // Map of appointment status to count
}