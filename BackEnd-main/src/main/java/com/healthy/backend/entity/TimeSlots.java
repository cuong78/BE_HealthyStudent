package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TimeSlots")
public class TimeSlots {

    @Id
    @Column(name = "TimeSlotsID", length = 36, nullable = false, unique = true)
    private String timeSlotsID;

    @Column(name = "SlotDate", nullable = false)
    private LocalDate slotDate;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PsychologistID", nullable = false)
    private Psychologists psychologist;

    @Column(name = "SlotNumber", nullable = false)
    private int slotNumber;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();
 
    public enum Status {
        Available,
        Booked
    }

    public TimeSlots(LocalDate date, LocalTime startTime, LocalTime endTime, Psychologists psychologist, int slotNumber) {
        this.slotDate = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = Status.Available;
        this.psychologist = psychologist;
        this.slotNumber = slotNumber;
        this.timeSlotsID = generateTimeSlotsID(psychologist.getPsychologistID(),date, slotNumber);
    }

    private String generateTimeSlotsID(String psychologistId, LocalDate date, int slotNumber) {
        String formattedDate = String.format("%02d%02d%02d",
                date.getDayOfMonth(),
                date.getMonthValue(),
                date.getYear() % 100); // Extract last two digits of the year
        return String.format("TS%s%s%02d", psychologistId, formattedDate, slotNumber);
    }
}