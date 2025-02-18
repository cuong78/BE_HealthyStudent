package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.*;
import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Appointments Controller", description = "Appointments related APIs.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Operation(
            summary = "Get all departments",
            description = "Returns a list of all departments."
    )
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> appointmentResponse = appointmentService.getAllDepartments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all appointments",
            description = "Returns a list of all appointments."
    )
    @GetMapping("/")
    public ResponseEntity<List<AppointmentResponse>> getAllPsychologist() {
        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get appointment by ID",
            description = "Returns the appointment with the specified ID."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponse);
    }

    @Operation(
            summary = "Book an appointment",
            description = "Creates a new appointment."
    )
    @PostMapping("/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(response);
    }








    @Operation(
            summary = "Request cancel of an appointment",
            description = "Requests an cancel update of an appointment."
    )
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.cancelAppointment(appointmentId);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        throw new OperationFailedException("Failed to cancel appointment");
    }

    @Operation(
            deprecated = true,
            summary = "Request update of an appointment",
            description = "Requests an update of an appointment."
    )
    @PutMapping("/{appointmentId}/update-request")
    public String requestUpdateAppointment(@PathVariable String appointmentId) {
        return "Appointment update requested";
    }

    @Operation(summary = "Check in to an appointment")
    @PostMapping("/{appointmentId}/check-in")
    public ResponseEntity<AppointmentResponse> checkIn(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.checkIn(appointmentId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check out from an appointment")
    @PostMapping("/{appointmentId}/check-out")
    public ResponseEntity<AppointmentResponse> checkOut(@PathVariable String appointmentId) {
        AppointmentResponse response = appointmentService.checkOut(appointmentId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            deprecated = true,
            summary = "Give feedback on an appointment",
            description = "Gives feedback on an appointment."
    )
    @PostMapping("/{appointmentId}/feedback")
    public String giveFeedback(@PathVariable String appointmentId, @RequestBody AppointmentFeedbackRequest feedback) {
        return "Feedback submitted successfully";
    }

    @Operation(
            summary = "Update an appointment",
            description = "Updates time slot, notes, or status of an appointment."
    )
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable String appointmentId,
                                    @RequestBody AppointmentUpdateRequest request) {
        AppointmentResponse response = appointmentService.updateAppointment(appointmentId, request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        throw new OperationFailedException("Failed to update appointment");
    }


    @Operation(
            deprecated = true,
            summary = "Make a report for an appointment",
            description = "Creates a report for an appointment."
    )
    @PostMapping("/{appointmentId}/report")
    public String makeReport(@PathVariable String appointmentId, @RequestBody AppointmentReportRequest report) {
        return "Report created successfully";
    }


    @Operation(
            deprecated = true,
            summary = "Get appointment details",
            description = "Returns detailed information about an appointment."
    )
    @GetMapping("/{appointmentId}/details")
    public String getAppointmentDetails(@PathVariable String appointmentId) {
        return "Detailed appointment information";
    }

}

