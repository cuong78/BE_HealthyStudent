package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Set;

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
    private Status status;

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
        status = Status.Active;
    }

    public Psychologists(String psychologistID, String userID, Integer yearsOfExperience, Status status,String departmentID) {
        this.psychologistID = psychologistID;
        this.userID = userID;
        this.yearsOfExperience = yearsOfExperience;
        this.status = status;
        this.departmentID = departmentID;
    }

    public enum Status {
        Active,
        OnLeave,
        Inactive
    }
} 