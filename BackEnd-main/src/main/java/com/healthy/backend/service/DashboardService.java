package com.healthy.backend.service;

import com.healthy.backend.dto.survey.StudentPsychologicalSummary;
import com.healthy.backend.dto.survey.SurveyHistoryDTO;
import com.healthy.backend.entity.SurveyResult;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.repository.SurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SurveyResultRepository resultRepository;

    public StudentPsychologicalSummary getStudentSummary(String studentId) {
        List<SurveyResult> results = resultRepository.findByStudentId(studentId);

        return StudentPsychologicalSummary.builder()
                .latestDepressionScore(getLatestScore(results, "DASS21", "depression"))
                .latestAnxietyScore(getLatestScore(results, "DASS21", "anxiety"))
                .latestStressScore(getLatestScore(results, "DASS21", "stress"))
                .cfqAverageScore(calculateAverageCFQ(results))
                .build();
    }

    private Integer getLatestScore(List<SurveyResult> results, String surveyType, String category) {
        return results.stream()
                .filter(r -> r.getSurvey().getSurveyType().equals(surveyType))
                .sorted(Comparator.comparing(SurveyResult::getCreatedAt).reversed())
                .findFirst()
                .map(r -> {
                    switch(category) {
                        case "depression": return r.getDepressionScore();
                        case "anxiety": return r.getAnxietyScore();
                        case "stress": return r.getStressScore();
                        default: return null;
                    }
                })
                .orElse(null);
    }

    public List<SurveyHistoryDTO> getSurveyHistory(String studentId, String surveyType) {
        List<SurveyResult> results = resultRepository.findByStudentId(studentId);

        return results.stream()
                .filter(r -> surveyType == null ||
                        r.getSurvey().getSurveyType().name().equalsIgnoreCase(surveyType))
                .map(result -> SurveyHistoryDTO.builder()
                        .surveyName(result.getSurvey().getSurveyName())
                        .surveyType(result.getSurvey().getSurveyType().name())
                        .completedDate(result.getCreatedAt())
                        .depressionScore(result.getDepressionScore())
                        .anxietyScore(result.getAnxietyScore())
                        .stressScore(result.getStressScore())
                        .totalScore(result.getTotalScore())
                        .severityLevels(calculateSeverityLevels(result))
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, String> calculateSeverityLevels(SurveyResult result) {
        Map<String, String> levels = new HashMap<>();
        if (result.getSurvey().getSurveyType() == Surveys.SurveyType.DASS21) {
            levels.put("Depression", getDASS21Severity(result.getDepressionScore(), "Depression"));
            levels.put("Anxiety", getDASS21Severity(result.getAnxietyScore(), "Anxiety"));
            levels.put("Stress", getDASS21Severity(result.getStressScore(), "Stress"));
        } else {
            levels.put("CFQ", interpretCFQScore(result.getTotalScore()));
        }
        return levels;
    }
}