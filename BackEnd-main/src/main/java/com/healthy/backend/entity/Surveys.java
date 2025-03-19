package com.healthy.backend.entity;

import com.healthy.backend.enums.SurveyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Surveys")
public class Surveys {

    @Id
    @Column(name = "SurveyID", length = 36, nullable = false)
    private String surveyID;

    @Column(name = "SurveyName", length = 100, nullable = false)
    private String surveyName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "Details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "Duration", length = 100)
    private String duration;

    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Column(name = "CreatedBy", length = 36, nullable = false)
    private String createdBy;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, nullable = false)
    private SurveyStatus status;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users creator;

    @OneToMany(mappedBy = "surveys", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications> notifications;

    @Enumerated(EnumType.STRING)
    @Column(name = "SurveyType", nullable = false)
    private SurveyType surveyType; // NEW FIELD

    public enum SurveyType {
        DASS21, CFQ
    }

    public Surveys(String surveyID, String surveyName, String description, String categoryID, String createdBy, SurveyStatus status) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.description = description;
        this.categoryID = categoryID;
        this.createdBy = createdBy;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
} 