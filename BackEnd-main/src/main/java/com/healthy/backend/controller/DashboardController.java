package com.healthy.backend.controller;

import com.healthy.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    // Get student's psychological summary
    @GetMapping("/students/{studentId}/summary")
    public ResponseEntity<StudentPsychologicalSummary> getStudentSummary(
            @PathVariable String studentId
    ) {
        return ResponseEntity.ok(dashboardService.getStudentSummary(studentId));
    }

    // Get trends over time
    @GetMapping("/students/{studentId}/history")
    public ResponseEntity<List<SurveyHistoryDTO>> getSurveyHistory(
            @PathVariable String studentId,
            @RequestParam(required = false) String surveyType
    ) {
        return ResponseEntity.ok(dashboardService.getSurveyHistory(studentId, surveyType));
    }
}