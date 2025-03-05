package com.healthy.backend.dto.manager;

import lombok.Data;

@Data
public class PsychologistStatsResponse {
    private String psychologistId;
    private String fullName;
    private double averageRating;
    private long appointmentCount;
}