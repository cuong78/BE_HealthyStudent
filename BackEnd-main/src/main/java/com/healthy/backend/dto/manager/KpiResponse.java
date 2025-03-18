package com.healthy.backend.dto.manager;

import lombok.Data;

@Data
public class KpiResponse {
    private String psychologistId;
    private int month;
    private int year;
    private int targetSlots;
    private String message;

    public KpiResponse(String psychologistId, int month, int year, int targetSlots, String message) {
        this.psychologistId = psychologistId;
        this.month = month;
        this.year = year;
        this.targetSlots = targetSlots;
        this.message = message;
    }
}