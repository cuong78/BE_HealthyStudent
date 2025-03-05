package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for feedback

    @Column(name = "Comment", columnDefinition = "TEXT")
    private String comment; // The feedback comment

    @Column(name = "Rating")
    private Integer rating; // Rating associated with the feedback

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "AppointmentID", nullable = false)
    private Appointments appointment; // Reference to the appointment

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}