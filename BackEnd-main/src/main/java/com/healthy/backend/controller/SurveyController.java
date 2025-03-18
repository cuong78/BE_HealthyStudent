package com.healthy.backend.controller;

import com.healthy.backend.dto.survey.*;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {

    private final SurveyService surveyService;
    private final TokenService tokenService;

    @Operation(
            summary = "Get all surveys",
            description = "Returns a list of available surveys."
    )
    @GetMapping()
    public ResponseEntity<List<SurveysResponse>> getAllSurveys(HttpServletRequest request) {
        Users user = tokenService.retrieveUser(request);
        List<SurveysResponse> surveys = surveyService.getAllSurveys(user);
        if (surveys.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(surveys);
    }

    @Operation(
            summary = "Get score in survey",
            description = "Return the score that the student achieved in the survey"
    )
    @PostMapping("/options/scoreResult")
    public ResponseEntity<?> getScoreFromStudentInSuv(
            @RequestParam String surveyId,
            @RequestBody List<String> optionId,
            @RequestParam(required = false) String studentId) {
        StatusStudent status = surveyService.getScoreFromStudentInSuv(surveyId, optionId, studentId);
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Get survey details",
            description = "Returns details for a specific survey."
    )
    @GetMapping("/questions")
    public ResponseEntity<?> getSurveyDetails(@RequestParam String surveyId) {
        SurveyQuestionResponse surveyQuestions = surveyService.getSurveyQuestion(surveyId);
        if (surveyQuestions == null) {
            throw new ResourceNotFoundException("No survey questions found");
        }
        return ResponseEntity.ok(surveyQuestions);
    }

    @Operation(
            summary = "Update question in survey",
            description = "Updates a question in a survey."
    )
    @PutMapping("/questions")
    public ResponseEntity<?> updateSurveyQuestion(
            @RequestParam String surveyId,
            @Valid @RequestBody SurveyQuestionResponse surveyQuestionResponse

    ) {
        try {
            surveyService.updateSurveyQuestion(surveyId, surveyQuestionResponse);
            return ResponseEntity.ok("Survey question updated sucessfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the survey question" + ex.getMessage());
        }
    }



    @Operation(

            summary = "Get survey results",
            description = "Returns results for a specific survey."
    )
    @GetMapping("/result")
    public ResponseEntity<?> getSurveyResults(HttpServletRequest request,
                                              @RequestParam String surveyId) {
        SurveyResultsResponse surveyResult = surveyService.getSurveyResultsBySurveyID(request, surveyId);
        return ResponseEntity.ok(surveyResult);
    }



    @Operation(
            summary = "Create survey",
            description = "Creates a new survey."
    )
    @PostMapping("/create")  // Manager or Psychologist Only
    public ResponseEntity<SurveyRequest> createSurvey(
            @Valid @RequestBody SurveyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SurveyRequest());
    }


    @Operation(
            summary = "Add question to survey",
            description = "Adds a question to a survey."
    )
    @PostMapping("/questions")
    public ResponseEntity<?> addSurveyQuestion(HttpServletRequest request,
                                               @RequestParam String surveyId,
                                               @RequestBody SurveyQuestionResponse question) {
        try {
            surveyService.addSurveyQuestion(request, surveyId, question);
            return ResponseEntity.ok("Survey question add successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the survey question" + ex.getMessage());
        }
    }



    @Operation(
            summary = "Add answer to question",
            description = "Adds an answer to a question."
    )
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<?> addAnswerToQuestion(@RequestParam String surveyId, @PathVariable String questionId, @RequestBody List<QuestionOption> answer) {
        try {
            surveyService.addAnswerToQuestion(surveyId, questionId, answer);
            
            return ResponseEntity.ok("List of answers add sucessfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding list of answers  " + ex.getMessage());
        }
    }

    @Operation(
            summary = "Get student survey results",
            description = "Returns results for a specific student's survey."
    )
    @GetMapping("/results/student")
    public ResponseEntity<?> getStudentIDSurveyResults(
            HttpServletRequest request,
            @RequestParam String surveyId,
            @RequestParam(required = false) String studentId) {
        String studentID = tokenService.validateRequestStudentID(request, studentId);
        SurveyQuestionResponse surveyResponse = surveyService.getSurveyResultByStudentID(request, surveyId, studentID);
        return ResponseEntity.ok(surveyResponse);
    }



    @Operation(
            summary = "Update survey status",
            description = "Updates the status of a survey."
    )
    @PutMapping("/status")
    public ResponseEntity<?> updateSurveyStatus(
            @RequestParam String surveyId,
            @RequestBody SurveyRequest status) {
        try {
            surveyService.updateSurveyStatus(surveyId, status);
            return ResponseEntity.ok("Survey status updated successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating status of survey" + ex.getMessage());
        }
    }



    @GetMapping("/survey/{surveyId}/students/checkResultsToHaveAppointment")
    public ResponseEntity<?> getLowScoringStudentsForAppointment(
            HttpServletRequest request,
            @PathVariable String surveyId)
            {
        try {
            List<ConfirmationRequest> confirmationRequests = surveyService.getLowScoringStudentsForAppointment(request, surveyId);
            return ResponseEntity.ok(confirmationRequests);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request" + ex.getMessage());
        }
    }

    @PostMapping("/survey/{surveyId}/students/appointments")
    public ResponseEntity<?> handleAppointmentRequest(
            @RequestBody List<ConfirmationRequest> requests)
            {
        try {
            
            return ResponseEntity.ok(surveyService.handleAppointmentRequest(requests) ? "You can make appointment now" : "");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request" + ex.getMessage());
        }
    }

}
