package com.healthy.backend.entity;

import com.healthy.backend.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private AppointmentStatus status;

    @Column(name = "StudentNote", columnDefinition = "TEXT")
    private String studentNote;

    @Column(name = "PsychologistNote", columnDefinition = "TEXT")
    private String psychologistNote;



    @Column(name = "Rating")
    private Integer rating;



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



    @Column(name = "CancellationReason", columnDefinition = "TEXT")
    private String cancellationReason;


    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications > notifications;

    public Appointments() {
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Appointments(String appointmentID, String timeSlotsID, String studentID, String psychologistID, AppointmentStatus status) {
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

}