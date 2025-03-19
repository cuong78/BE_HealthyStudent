package com.healthy.backend.service;


import com.healthy.backend.dto.survey.*;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyResultRepository resultRepository;

    public SurveyResultDTO processSurveySubmission(String surveyId, SurveySubmissionRequest request) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Validate questions and calculate scores
        if(survey.getSurveyType() == Surveys.SurveyType.DASS21) {
            return processDASS21Submission(request, survey);
        } else {
            return processCFQSubmission(request, survey);
        }
    }

    private SurveyResultDTO processDASS21Submission(SurveySubmissionRequest request, Surveys survey) {
        Map<String, Integer> groupScores = new HashMap<>();

        for (QuestionAnswer answer : request.getAnswers()) {
            SurveyQuestions question = survey.getQuestions().stream()
                    .filter(q -> q.getQuestionID().equals(answer.getQuestionId()))
                    .findFirst().orElseThrow();

            SurveyQuestionOptions option = question.getOptions().stream()
                    .filter(o -> o.getOptionID().equals(answer.getOptionId()))
                    .findFirst().orElseThrow();

            String group = question.getQuestionGroup();
            groupScores.put(group, groupScores.getOrDefault(group, 0) + option.getScore());
        }

        // Calculate final scores (double the sum)
        int depression = groupScores.getOrDefault("Depression", 0) * 2;
        int anxiety = groupScores.getOrDefault("Anxiety", 0) * 2;
        int stress = groupScores.getOrDefault("Stress", 0) * 2;

        // Save result
        SurveyResult result = new SurveyResult();
        result.setDepressionScore(depression);
        result.setAnxietyScore(anxiety);
        result.setStressScore(stress);
        result.setSurveyID(survey.getSurveyID());
        result.setStudentID(request.getStudentId());
        resultRepository.save(result);

        return mapToDTO(result);
    }

    public List<SurveysResponse> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SurveyDetailDTO getSurveyWithQuestions(String surveyId) {
        Surveys survey = surveyRepository.findByIdWithQuestions(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        return mapToDetailDTO(survey);
    }

    private SurveysResponse mapToDTO(Surveys survey) {
        return SurveysResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategoryID().toString())
                .surveyType(survey.getSurveyType())
                .createdAt(survey.getCreatedAt())
                .build();
    }

    private SurveyDetailDTO mapToDetailDTO(Surveys survey) {
        return SurveyDetailDTO.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .surveyType(survey.getSurveyType())
                .questions(survey.getQuestions().stream()
                        .map(q -> SurveyDetailDTO.QuestionDTO.builder()
                                .questionId(q.getQuestionID())
                                .questionText(q.getQuestionText())
                                .questionGroup(q.getQuestionGroup())
                                .options(q.getOptions().stream()
                                        .map(o -> SurveyDetailDTO.QuestionDTO.OptionDTO.builder()
                                                .optionId(o.getOptionID())
                                                .optionText(o.getOptionText())
                                                .score(o.getScore())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


}