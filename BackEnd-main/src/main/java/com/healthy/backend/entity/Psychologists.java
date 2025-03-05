package com.healthy.backend.entity;

import com.healthy.backend.enums.PsychologistStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "Psychologists")
public class Psychologists {

    @Id
    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "YearsOfExperience")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private PsychologistStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    @Column(name = "DepartmentID", length = 36)
    private String departmentID; // Only store the Department ID here

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DepartmentID", referencedColumnName = "DepartmentID", insertable = false, updatable = false) // Establish foreign key relationship
    private Department department;

    public String getFullNameFromUser() {
        if (user != null) {
            return user.getFullName();
        }
        return null;
    }

    public Psychologists() {
        status = PsychologistStatus.ACTIVE;
    }

    public Psychologists(String psychologistID, String userID, Integer yearsOfExperience, PsychologistStatus status,String departmentID) {
        this.psychologistID = psychologistID;
        this.userID = userID;
        this.yearsOfExperience = yearsOfExperience;
        this.status = status;
        this.departmentID = departmentID;
    }
} 