package com.healthy.backend.dto.survey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentPsychologicalSummary {
    private Double latestDepressionScore;
    private String depressionSeverity;
    private Double latestAnxietyScore;
    private String anxietySeverity;
    private Double latestStressScore;
    private String stressSeverity;
    private Double cfqAverageScore;
    private String cfqInterpretation;
}