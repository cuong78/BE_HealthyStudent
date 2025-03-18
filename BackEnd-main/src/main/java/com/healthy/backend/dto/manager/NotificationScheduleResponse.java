package com.healthy.backend.dto.manager;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class NotificationScheduleResponse {
    private LocalTime notificationTime;
    private DayOfWeek notificationDay;
    private String message;

    public NotificationScheduleResponse(LocalTime notificationTime, DayOfWeek notificationDay, String message) {
        this.notificationTime = notificationTime;
        this.notificationDay = notificationDay;
        this.message = message;
    }
}