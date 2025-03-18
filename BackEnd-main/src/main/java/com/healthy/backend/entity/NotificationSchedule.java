package com.healthy.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "notification_schedule")
@Getter
@Setter
public class NotificationSchedule {
    @Id
    private String id;

    @Column(name = "notification_time")
    private LocalTime notificationTime;

    @Column(name = "notification_day")
    private DayOfWeek notificationDay;

}
