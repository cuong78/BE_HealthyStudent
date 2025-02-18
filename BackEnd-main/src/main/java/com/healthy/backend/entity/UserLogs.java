package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserLogs")
public class UserLogs {

    @Id
    @Column(name = "LogID", length = 36)
    private String logID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "LoginTime", updatable = false)
    private LocalDateTime loginTime;

    @Column(name = "IPAddress", length = 50)
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public UserLogs(String logID, String userId, String ipAddress) {
        this.logID = logID;
        this.userID = userId;
        this.ipAddress = ipAddress;
    }

    @PrePersist
    protected void onCreate() {
        loginTime = LocalDateTime.now();
    }
} 