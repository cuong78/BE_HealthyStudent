package com.healthy.backend.entity;

import com.healthy.backend.entity.Enum.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Appointments")
public class Appointments {

    @Id
    @Column(name = "AppointmentID", length = 36)
    private String appointmentID;

    @Column(name = "TimeSlotsID", length = 36)
    private String timeSlotsID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private StatusEnum status;

    @Column(name = "StudentNote", columnDefinition = "TEXT")
    private String studentNote;

    @Column(name = "PsychologistNote", columnDefinition = "TEXT")
    private String psychologistNote;

    @Column(name = "Feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "TimeSlotsID", insertable = false, updatable = false)
    private TimeSlots timeSlot;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

    @Column(name = "CheckInTime")
    private LocalDateTime checkInTime;

    @Column(name = "CheckOutTime")
    private LocalDateTime checkOutTime;

    public Appointments() {
        this.status = StatusEnum.Scheduled;
    }

    public Appointments(String appointmentID, String timeSlotsID, String studentID, String psychologistID, StatusEnum status) {
        this.appointmentID = appointmentID;
        this.timeSlotsID = timeSlotsID;
        this.studentID = studentID;
        this.psychologistID = psychologistID;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppointmentType {
        Online,
        Offline
    }


}