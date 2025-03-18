package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Students")
public class Students {

    @Id
    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    // Nullable, student can exist without a parent
    @Column(name = "ParentID", length = 36, nullable = true)
    private String parentID;

    @Column(name = "Grade")
    private Integer grade;

    @Column(name = "ClassName", length = 20)
    private String className;

    @Column(name = "SchoolName", length = 100)
    private String schoolName;

    @Column(name = "AnxietyScore", precision = 5, scale = 2)
    private BigDecimal anxietyScore;

    @Column(name = "StressScore", precision = 5, scale = 2)
    private BigDecimal stressScore;

    @Column(name = "DepressionScore", precision = 5, scale = 2)
    private BigDecimal depressionScore;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ParentID", referencedColumnName = "ParentID", insertable = false, updatable = false)
    private Parents parents;

    public Students(String studentID, String userID, Integer grade, String className, String schoolName, BigDecimal anxietyScore, BigDecimal depressionScore, BigDecimal stressScore) {
        this.studentID = studentID;
        this.userID = userID;
        this.grade = grade;
        this.className = className;
        this.schoolName = schoolName;
        this.depressionScore = depressionScore;
        this.anxietyScore = anxietyScore;
        this.stressScore = stressScore;
    }
    public Students(String studentID, String userID, String parentID, Integer grade, String className, String schoolName, BigDecimal anxietyScore, BigDecimal depressionScore, BigDecimal stressScore) {
        this.studentID = studentID;
        this.userID = userID;
        this.parentID = parentID;
        this.grade = grade;
        this.className = className;
        this.schoolName = schoolName;
        this.depressionScore = depressionScore;
        this.anxietyScore = anxietyScore;
        this.stressScore = stressScore;
    }
} 