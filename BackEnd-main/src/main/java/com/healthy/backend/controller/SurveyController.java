package com.healthy.backend.controller;

import com.healthy.backend.dto.survey.*;

import com.healthy.backend.service.SurveyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
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

    // Get all surveys
    @GetMapping
    public ResponseEntity<List<SurveyResultDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    // Get survey details with questions
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDetailDTO> getSurveyById(@PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyWithQuestions(surveyId));
    }

    // Submit survey results
    @PostMapping("/{surveyId}/submit")
    public ResponseEntity<SurveyResultDTO> submitSurvey(
            @PathVariable String surveyId,
            @RequestBody SurveySubmissionRequest request
    ) {
        return ResponseEntity.ok(surveyService.processSurveySubmission(surveyId, request));
    }
}
