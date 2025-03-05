package com.healthy.backend.controller;

import com.healthy.backend.dto.comment.CommentRequest;
import com.healthy.backend.dto.comment.CommentResponse;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "Endpoints for managing comments")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {

    private final CommentService commentService;
    private final TokenService tokenService;

    @PostMapping("/appointments/comments/add")
    public ResponseEntity<CommentResponse> addComment(
            @RequestParam String appointmentId,
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest httpRequest) {
        String userId = tokenService.retrieveUser(httpRequest).getUserId();
        CommentResponse response = commentService.addAppointmentComment(appointmentId, request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointments/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForAppointment(@RequestParam String appointmentId) {
        List<CommentResponse> responses = commentService.getCommentsForAppointment(appointmentId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/programs/comments/add")
    public ResponseEntity<CommentResponse> addProgramComment(
            @RequestParam String programId,
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest httpRequest) {
        String userId = tokenService.retrieveUser(httpRequest).getUserId();
        CommentResponse response = commentService.addProgramComment(programId, request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/programs/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForProgram(@RequestParam String program) {
        List<CommentResponse> responses = commentService.getCommentsForProgram(program);
        return ResponseEntity.ok(responses);
    }
}
