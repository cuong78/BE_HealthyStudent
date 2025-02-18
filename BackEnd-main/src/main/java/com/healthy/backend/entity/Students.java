package com.healthy.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "ParentID", length = 36, nullable = false)
    private String parentID;

    @Column(name = "FullName", length = 100, nullable = false)
    private String fullName;

    @Schema(example = "student@example.com")
    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "Grade")
    private Integer grade;

    @Column(name = "ClassName", length = 20)
    private String className;

    @Column(name = "SchoolName", length = 100)
    private String schoolName;

    @Column(name = "AnxietyScore")
    private Integer anxietyScore;

    @Column(name = "StressScore")
    private Integer stressScore;

    @Column(name = "DepressionScore")
    private Integer depressionScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ParentID", referencedColumnName = "ParentID", insertable = false, updatable = false)
    private Parents parents;

    public Students(String studentID, String userID, String parentID, Integer grade, String className, String schoolName, Integer anxietyScore, Integer depressionScore, Integer stressScore) {
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