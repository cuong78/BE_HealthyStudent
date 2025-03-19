package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PsychologicalIndicators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PsychologicalIndicators {

    @Id
    @Column(name = "IndicatorID", length = 36)
    private String indicatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", referencedColumnName = "StudentID")
    private Students student;

    @Enumerated(EnumType.STRING)
    @Column(name = "SurveyType", nullable = false)
    private SurveyType surveyType;

    @Column(name = "DepressionScore")
    private Integer depressionScore;

    @Column(name = "AnxietyScore")
    private Integer anxietyScore;

    @Column(name = "StressScore")
    private Integer stressScore;

    @Column(name = "TotalScore")
    private Integer totalScore;

    @Column(name = "RecordDate", nullable = false)
    private LocalDateTime recordDate;

    public enum SurveyType {
        DASS21, CFQ
    }
}
