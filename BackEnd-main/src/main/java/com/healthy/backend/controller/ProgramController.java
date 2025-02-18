package com.healthy.backend.controller;

import com.healthy.backend.dto.programs.ProgramParticipationRequest;
import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsRequest;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Programs Controller", description = "Program related APIs")
public class ProgramController {

    private final ProgramService programService;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////GET REQUESTS//////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Get all programs
    @ApiResponse(
            responseCode = "200",
            description = "Programs retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProgramsResponse.class),
                    examples = @ExampleObject(
                            name = "Success Response",
                            value = "{ \"status\": \"success\", \"data\": [ { \"programId\": \"P001\", \"name\": \"Program A\", \"status\": \"Active\" }, { \"programId\": \"P002\", \"name\": \"Program B\", \"status\": \"Inactive\" } ] }"
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid program ID format",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Bad Request Example",
                            value = "{\"error\": \"Invalid program ID format\", \"status\": 400}"
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Internal Server Error Example",
                            value = "{\"error\": \"Unexpected error occurred\", \"status\": 500}"
                    )
            )
    )
    @Operation(summary = "Get all programs", description = "Returns a list of all programs.")
    @GetMapping()
    public ResponseEntity<?> getPrograms() {
        List<ProgramsResponse> programsResponseList = programService.getAllPrograms();
        if (programsResponseList.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return ResponseEntity.ok(programsResponseList);
    }

    // Get all program details
    // Get all programs
    @ApiResponse(
            responseCode = "200",
            description = "Programs retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProgramsResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400", description = "Invalid program ID format"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
    )
    @Operation(summary = "Get all program statuses", description = "Returns a list of all program statuses.")
    @GetMapping("/details")
    public ResponseEntity<?> getAllProgramStatuses() {
        List<ProgramsResponse> programsResponseList = programService.getAllProgramsDetails();
        if (programsResponseList.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return ResponseEntity.ok(programsResponseList);
    }

    // Get program details
    @Operation(summary = "Get program details", description = "Returns details of a specific program.")
    @GetMapping("/{programId}/details")
    public ResponseEntity<?> getProgramDetails(@PathVariable String programId) {
        ProgramsResponse programsResponse = programService.getProgramById(programId);
        if (programsResponse == null) throw new ResourceNotFoundException("Program not found");
        return ResponseEntity.ok(programsResponse);
    }

    // Get program location
    @Operation(deprecated = true, summary = "Get program location", description = "Returns the location of a specific program.")
    @GetMapping("/{programId}/location")
    public ResponseEntity<?> getProgramLocation(@PathVariable String programId) {
        return ResponseEntity.ok("Location for program " + programId);
    }

    // Get program status
    @Operation(summary = "Get program status", description = "Returns the status of a specific program.")
    @GetMapping("/{programId}/status")
    public ResponseEntity<?> getRegistrationStatus(@PathVariable String programId) {
        String status = programService.getProgramStatus(programId);
        if (status.isEmpty()) {
            throw new ResourceNotFoundException("Program not found");
        }
        return ResponseEntity.ok("Status for program " + programId + " is " + status);
    }

    // Get enrolled programs of a student
    @Operation(summary = "Get enrolled programs of a student", description = "Returns a list of enrolled programs.")
    @GetMapping("/enrolled/{studentId}")
    public ResponseEntity<?> getEnrolledPrograms(@PathVariable String studentId) {
        List<ProgramsResponse> programsResponseList = programService.getEnrolledPrograms(studentId);
        if (programsResponseList.isEmpty()) throw new ResourceNotFoundException("No enrolled programs found");
        return ResponseEntity.ok(programsResponseList);
    }

    // Get program participants
    @Operation(summary = "Get program participants", description = "Returns a list of participants for a specific program.")
    @GetMapping("/{programId}/participants")
    public ResponseEntity<?> getProgramParticipants(@PathVariable String programId) {
        List<ProgramParticipationResponse> programParticipationResponseList = programService.getProgramParticipants(programId);
        if (programParticipationResponseList.isEmpty()) throw new ResourceNotFoundException("No participants found");
        return ResponseEntity.ok(programParticipationResponseList);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////POST REQUESTS/////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Register for a program
    @Operation(summary = "Register for a program", description = "Registers a student for a program.")
    @PostMapping("/{programId}/register")
    public ResponseEntity<?> registerForProgram(@RequestBody ProgramParticipationRequest programParticipationRequest) {
        if (programService.registerForProgram(programParticipationRequest)) {
            return ResponseEntity.ok("Registration successful for program " + programParticipationRequest.getProgramID());
        }
        throw new ResourceNotFoundException("Failed to register for program");
    }

    // Submit feedback
    @Operation(deprecated = true, summary = "Submit feedback", description = "Submits feedback for a specific program.")
    @PostMapping("/{programId}/feedback")
    public ResponseEntity<?> submitFeedback(@PathVariable String programId) {
        return ResponseEntity.ok("Feedback submitted for program " + programId);
    }

    // Create a new program
    @Operation(summary = "Create a new program", description = "Creates a new program.")
    @PostMapping("/create")
    public ResponseEntity<?> createProgram(@RequestBody ProgramsRequest programsRequest) {
        ProgramsResponse programsResponse = programService.createProgram(programsRequest);
        if (programsResponse.getProgramID() == null) throw new OperationFailedException("Failed to create program");
        return ResponseEntity.status(HttpStatus.CREATED).body(programsResponse);
    }

//    // Very low priority
//    // Send program announcement
//    @Operation(deprecated = true, summary = "Send program announcement", description = "Sends an announcement for a specific program.")
//    @PostMapping("/{programId}/announcement")
//    public ResponseEntity<?> sendProgramAnnouncement(@PathVariable String programId) {
//        return ResponseEntity.ok("Announcement sent for program " + programId);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////PUT REQUESTS//////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Update a program
    @Operation(summary = "Update a program", description = "Updates an existing program.")
    @PutMapping("/{programId}/edit")
    public ResponseEntity<?> updateProgram(@PathVariable String programId) {
        return ResponseEntity.ok("Program updated " + programId);
    }

    // Cancel registration
    @Operation(summary = "Cancel registration for a program", description = "Cancels registration for a program.")
    @PutMapping("/{programId}/cancel-request")
    public ResponseEntity<String> cancelParticipation(@RequestBody ProgramParticipationRequest programParticipationRequest) {
        boolean isCancelled = programService.cancelParticipation(programParticipationRequest);
        if (isCancelled) {
            return ResponseEntity.ok("Participation successfully cancelled.");
        }
        throw new OperationFailedException("Failed to cancel participation.");
    }

//    // Very low priority
//    // Approve cancellation
//    @Operation(deprecated = true, summary = "Approve cancellation request", description = "Approves a cancellation request for a specific program.")
//    @PutMapping("/{programId}/approve-cancel/{studentId}")
//    public ResponseEntity<?> approveCancelRequest(@PathVariable String programId, @PathVariable String studentId) {
//        return ResponseEntity.ok("Cancellation request processed for student " + studentId);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////DELETE REQUESTS////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Delete a program
    @Operation(summary = "Delete a program", description = "Deletes an existing program.")
    @DeleteMapping("/{programId}/delete")
    public ResponseEntity<?> deleteProgram(@PathVariable String programId) {
        if (!programService.deleteProgram(programId)) throw new ResourceNotFoundException("Program not found");
        return ResponseEntity.noContent().build();
    }
}