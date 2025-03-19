package com.healthy.backend.dto.survey;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class SurveyHistoryDTO {
    private String surveyName;
    private String surveyType;
    private LocalDateTime completedDate;
    private Integer depressionScore;
    private Integer anxietyScore;
    private Integer stressScore;
    private Integer totalScore;
    private Map<String, String> severityLevels;
}
