package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Notifications")
public class Notifications {

    @Id
    @Column(name = "NotificationID", length = 36)
    private String notificationID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "Title", length = 255, nullable = false)
    private String title;

    @Column(name = "Message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false, columnDefinition = "ENUM('Appointment', 'Survey', 'Program', 'Done')")
    private Type type;

    @Column(name = "IsRead")
    private Boolean isRead;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public Notifications() {
        isRead = false;
    }

    public Notifications(String notificationID, String userID, String title, String message, Type type) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Type {
        Appointment,
        Survey,
        Program,
        Done
    }
} 