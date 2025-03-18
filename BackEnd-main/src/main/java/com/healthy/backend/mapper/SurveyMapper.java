package com.healthy.backend.mapper;

import com.healthy.backend.dto.survey.*;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SurveyMapper {


    public SurveysResponse buildManagerSurveysResponse(Surveys survey, int numberOfQuestions, String completeStatus, String count) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .numberOfQuestions(numberOfQuestions)
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .studentComplete(count)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey, int numberOfQuestions, String completeStatus, String score) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .numberOfQuestions(numberOfQuestions)
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .score(score)
                .build();
    }
    public SurveysResponse buildSurveysResponse1(Surveys survey, int numberOfQuestions, String completeStatus, List<StatusStudent> status) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .numberOfQuestions(numberOfQuestions)
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .statusStudent(status)
                .build();
        }           


    public StatusStudent maptoResultStudent1(String status, String score, String studentId) {
        return StatusStudent.builder()
                .score(score)
                .studentId(studentId)
                .build();
    }

//     public StatusStudent maptoResultStudent2(String status, String score, String studentId, String complete) {
//         return StatusStudent.builder()
//                 .score(score)
//                 .studentId(studentId)
//                 .completeStatus(complete)
//                 .build();
//     }

    public SurveyResultsResponse mapToListResultsResponse(Surveys survey, List<StatusStudent> std) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .status((survey.getStatus().name()))
                .std(std)
                .build();
    }

    public List<SurveyResultsResponse> getUserSurveyResults(List<SurveyQuestionOptionsChoices> surveyResults) {
        if (surveyResults == null) throw new ResourceNotFoundException("No survey results found");
        return buildSurveyResults(surveyResults);
    }

    public List<SurveyResultsResponse> buildSurveyResults(List<SurveyQuestionOptionsChoices> surveyResults) {

        Map<String, List<SurveyQuestionResultResponse>> groupedResults =
                surveyResults
                        .stream()
                        .collect(Collectors.groupingBy(
                                result ->
                                        result.getQuestion().getSurveyID(),
                                Collectors.mapping(this::mapToSurveyQuestionResultResponse, Collectors.toList())
                        ));

        Map<String, Surveys> surveyMap = groupedResults.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> new Surveys()));
        return groupedResults.entrySet().stream()
                .map(entry -> mapToSurveyResultsResponse(surveyMap.get(entry.getKey()), entry.getValue()))
                .toList();
    }


    public SurveyQuestionResultResponse mapToSurveyQuestionResultResponse(SurveyQuestionOptionsChoices result) {
        return SurveyQuestionResultResponse.builder()
                .questionId(result.getQuestionID())
                .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName())) // No LazyInitializationException
                .questionText(result.getQuestion().getQuestionText())
                .resultId(result.getOptionsChoicesID())
                .answerId(result.getOptions().getOptionID())
                .answer(result.getOptions().getOptionText())
                .score(result.getOptions().getScore())
                .build();
    }

    public SurveyResultsResponse mapToSurveyResultsResponse(Surveys survey, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .status((survey.getStatus().name()))
                .questions(questions)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey, int numberOfQuestions) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .numberOfQuestions(numberOfQuestions)
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getUsername())
                .build();
    }


    public SurveyQuestionResponse buildSurveyQuestionResponse(
            List<QuestionResponse> questionResponseList,
            Surveys survey
    ) {
        return SurveyQuestionResponse.builder()
                .surveyId(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .questionList(questionResponseList)
                .build();
    }

    public SurveyQuestionResponse buildSurveyResultResponse(
            List<QuestionResponse> questionResponseList,
            Surveys survey, String completeStatus,String score
    ) {
        return SurveyQuestionResponse.builder()
                .surveyId(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .numberOfQuestions(questionResponseList.size())
                .questionList(questionResponseList)
                .completeStatus(completeStatus)
                .totalScore(score)
                .build();
    }

    public QuestionResponse buildQuestionResponse(
            List<QuestionOption> questionOption,
            SurveyQuestions surveyQuestions,
            Integer index) {
        return QuestionResponse.builder()
                .id(index.toString())
                .questionText(surveyQuestions.getQuestionText())
                .questionCategory(surveyQuestions.getCategory().getCategoryName().name())
                .questionOptions(questionOption)
                .build();
    }

    public QuestionOption buildNewQuestionOption(SurveyQuestionOptions options) {
        return QuestionOption.builder()
                .answerID(options.getOptionID())
                .label(options.getOptionText())
                .value(options.getScore())
                .checked(false)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createBy(survey.getCreator().getUsername())
                .build();
    }
}