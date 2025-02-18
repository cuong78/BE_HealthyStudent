package com.healthy.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthy.backend.dto.survey.SurveyQuestionResult;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.SurveyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {
    private final SurveyService surveyService;

    @Operation(

            summary = "Get all surveys",
            description = "Returns a list of available surveys."
    )
    @GetMapping()
    public ResponseEntity<List<SurveyResultsResponse>> getAllSurveys() {

        List<SurveyResultsResponse> surveyResultsResponses = surveyService.getAllSurveyResults();
        if(!surveyResultsResponses.isEmpty()) {
            return ResponseEntity.ok(surveyResultsResponses);
        }
            return ResponseEntity.noContent().build();



    }

    @Operation(
            deprecated = true,
            summary = "Get survey details",
            description = "Returns details for a specific survey."
    )
    @GetMapping("/{surveyId}")
    public String getSurveyDetails(@PathVariable String surveyId) {
        return "Survey details for ID " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Submit survey response",
            description = "Submits a response to a survey."
    )
    @PostMapping("/{surveyId}/take")
    public String submitSurveyResponse(@PathVariable String surveyId, @RequestBody String responses) {
        return "Survey responses saved for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Get survey results",
            description = "Returns results for a specific survey."
    )
    @GetMapping("/{surveyId}/result")
    public String getSurveyResults(@PathVariable String surveyId) {
        return "Survey results for ID " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Submit survey feedback",
            description = "Submits feedback for a survey."
    )
    @PostMapping("/{surveyId}/feedback")
    public String submitSurveyFeedback(@PathVariable String surveyId, @RequestBody String feedback) {
        return "Feedback submitted for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Create survey",
            description = "Creates a new survey."
    )
    @PostMapping("/create")
    public String createSurvey(@RequestBody String surveyDetails) {
        return "New survey created successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Update survey",
            description = "Updates an existing survey."
    )
    @PutMapping("/{surveyId}/update")
    public String updateSurvey(@PathVariable String surveyId, @RequestBody String surveyDetails) {
        return "Survey updated successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Cancel survey",
            description = "Cancels a survey."
    )
    @DeleteMapping("/{surveyId}/cancel")
    public String cancelSurvey(@PathVariable String surveyId) {
        return "Survey canceled successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Add question to survey",
            description = "Adds a question to a survey."
    )
    @PostMapping("/{surveyId}/questions")
    public String addSurveyQuestion(@PathVariable String surveyId, @RequestBody String question) {
        return "Question added to survey " + surveyId;
    }

    @Operation(

        summary = "Update question in survey",
        description = "Updates a question in a survey."
    )
    @PutMapping("/{surveyId}/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<?> updateSurveyQuestion(
       @PathVariable String surveyId,
       @PathVariable String questionId,
       @PathVariable String answerId,
       @Valid @RequestBody SurveyQuestionResult surveyQuestionResult

    )
        {
            try {
                surveyService.updateSurveyQuestion(questionId, surveyId, answerId, surveyQuestionResult);
                return ResponseEntity.ok("Survey question updated sucessfully");

            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the survey question" + ex.getMessage());
            }
        }

    @Operation(
            deprecated = true,
            summary = "Delete question from survey",
            description = "Deletes a question from a survey."
    )
    @DeleteMapping("/{surveyId}/questions/{questionId}")
    public String deleteSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        return "Question removed from survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Add answer to question",
            description = "Adds an answer to a question."
    )
    @PostMapping("/{surveyId}/questions/{questionId}/answers")
    public String addAnswerToQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody String answer) {
        return "Answer added to question " + questionId + " in survey " + surveyId;
    }


    @Operation(
            deprecated = true,
            summary = "Get student survey results",
            description = "Returns results for a specific student's survey."
    )
    @GetMapping("/{surveyId}/students-results")
    public String getStudentSurveyResults(@PathVariable String surveyId) {
        return "Student survey results for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Get survey dashboard",
            description = "Returns a dashboard overview for surveys."
    )
    @GetMapping("/dashboard")
    public String getSurveyDashboard() {
        return "Survey dashboard overview";
    }

    @Operation(
            deprecated = true,
            summary = "Schedule survey",
            description = "Schedules a survey."
    )
    @PostMapping("/{surveyId}/schedule")
    public String scheduleSurvey(@PathVariable String surveyId, @RequestBody String scheduleDetails) {
        return "Survey " + surveyId + " scheduled successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Update survey status",
            description = "Updates the status of a survey."
    )
    @PutMapping("/{surveyId}/status")
    public String updateSurveyStatus(@PathVariable String surveyId, @RequestBody String status) {
        return "Survey status updated for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Enable anonymous survey",
            description = "Enables anonymous mode for a survey."
    )
    @PostMapping("/{surveyId}/anonymous")
    public String enableAnonymousSurvey(@PathVariable String surveyId) {
        return "Survey " + surveyId + " set to anonymous";
    }

    @Operation(
            deprecated = true,
            summary = "Export survey results",
            description = "Exports survey results in a specified format."
    )
    @GetMapping("/{surveyId}/export")
    public String exportSurveyResults(@PathVariable String surveyId, @RequestParam String format) {
        return "Survey results exported in format: " + format;
    }
}
