package com.healthy.backend.controller;


import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.timeslot.DefaultTimeSlotResponse;
import com.healthy.backend.dto.timeslot.TimeSlotBatchCreateRequest;
import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.AuthorizeException;
import com.healthy.backend.mapper.TimeSlotMapper;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.AppointmentService;
import com.healthy.backend.service.PsychologistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/psychologists")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Psychologist Controller", description = "Psychologist related APIs")
public class PsychologistController {

    private final TimeSlotMapper timeSlotMapper;
    private final AppointmentService appointmentService;
    private final PsychologistService psychologistService;
    private final TokenService tokenService;
    private final StudentRepository studentRepository;


    @Operation(summary = "Get all psychologists")
    @GetMapping()
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologist(HttpServletRequest request) {
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistDTO();
        return !psychologistResponse.isEmpty() ? ResponseEntity.ok(psychologistResponse) : ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get psychologist by ID")
    @GetMapping({ "/detail"})
    public ResponseEntity<PsychologistResponse> getPsychologistById(

            @RequestParam(required = false) String psychologistId,
            HttpServletRequest request) {

        Users user = tokenService.retrieveUser(request);
        String actualId = psychologistId;


        if (tokenService.validateRole(request, Role.MANAGER) && psychologistId == null) {
            throw new AuthorizeException("Psychologist ID is required for managers");
        }
        if (tokenService.validateRole(request, Role.STUDENT) ) {
            throw new AuthorizeException("Unauthorized access, Student can not view psychologists ");
        }
         if (psychologistId != null && !psychologistId.isEmpty()) {
                // Kiểm tra nếu Psychologist cố tình truyền ID khác
                PsychologistResponse current = psychologistService.getPsychologistByUserId(user.getUserId());
                if (!current.getPsychologistId().equals(psychologistId)) {
                    throw new AuthorizeException("You can only view your own profile");
                }
            }
            // Tự động lấy ID từ token nếu không truyền
            actualId = psychologistService.getPsychologistByUserId(user.getUserId()).getPsychologistId();


        // Xử lý cho Manager


        return ResponseEntity.ok(psychologistService.getPsychologistById(actualId));
    }


    @Operation(summary = "Update psychologist details")
    @PutMapping({ "/detail"})
    public ResponseEntity<PsychologistResponse> updatePsychologist(
            @RequestParam(required = false) String psychologistId,
            @RequestBody @Valid PsychologistRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        // Phân quyền
        if (tokenService.validateRole(httpRequest, Role.MANAGER) && psychologistId == null) {
            throw new AuthorizeException("Psychologist ID is required for managers");
        }

        if (tokenService.validateRole(httpRequest, Role.STUDENT)) {
            throw new AuthorizeException("Student not can update psychologist");
        }

        if (psychologistId == null) {
            psychologistId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
        }

        else {
            // Kiểm tra Psychologist chỉ update chính mình
            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
            if (!psychologistId.equals(actualId)) {
                throw new AuthorizeException("Unauthorized access,You can only update your own profile");
            }
        }

        return ResponseEntity.ok(psychologistService.updatePsychologist(
                psychologistId,
                request,
                currentUser.getUserId()
        ));
    }

    @Operation(
            summary = "Get all psychologists",
            description = "Returns a list of all registered psychologists filtered by specialization."
    )
    @GetMapping("/specializations")
    public ResponseEntity<List<PsychologistResponse>> getAllPsychologistByDepartment(
            @RequestParam(required = false) String department) { // Thêm parameter
        List<PsychologistResponse> psychologistResponse = psychologistService.getAllPsychologistByDepartment(department);
        if (!psychologistResponse.isEmpty()) {
            return ResponseEntity.ok(psychologistResponse);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all departments",
            description = "Returns a list of all departments.")
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> appointmentResponse = appointmentService.getAllDepartments();
        if (!appointmentResponse.isEmpty()) {
            return ResponseEntity.ok(appointmentResponse);
        }
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Create time slots from default templates")
    @PostMapping("/timeslots/batch")
    public ResponseEntity<List<TimeSlotResponse>> createTimeSlotsFromDefaults(
            @RequestParam(required = false) String psychologistId,
            @RequestBody @Valid TimeSlotBatchCreateRequest request,
            HttpServletRequest httpRequest) {

        Users currentUser = tokenService.retrieveUser(httpRequest);

        if (tokenService.validateRole(httpRequest, Role.STUDENT) ) {
            throw new AuthorizeException("Unauthorized access, Student can not create timeSlot ");
        }

        if (psychologistId == null) {
            psychologistId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
        }


        if (tokenService.validateRole(httpRequest, Role.PSYCHOLOGIST)) {
            // Kiểm tra Psychologist chỉ được tạo slot cho chính mình
            String actualId = psychologistService.getPsychologistIdByUserId(currentUser.getUserId());
            if (!psychologistId.equals(actualId)) {
                throw new AuthorizeException("Unauthorized to create slots for other psychologists");
            }
        }

        List<TimeSlotResponse> responses = psychologistService.createTimeSlotsFromDefaults(
                psychologistId,
                request.getSlotDate(),
                request.getDefaultSlotIds()
        );
        return ResponseEntity.ok(responses);


    }


    @Operation(summary = "Lấy danh sách time slots")
    @GetMapping("/timeslots")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlots(
            @RequestParam(required = false) String psychologistId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {

        Users currentUser = tokenService.retrieveUser(request);
        String studentId = null;

        // Nếu là student, lấy studentID
        if (currentUser.getRole() == Role.STUDENT) {
            Students student = studentRepository.findByUserID(currentUser.getUserId());
            studentId = student.getStudentID();
        }

        List<TimeSlotResponse> slots = psychologistService.getPsychologistTimeSlots(psychologistId, date, studentId);
        return ResponseEntity.ok(slots);
    }

    @Operation(summary = "Get default time slots")
    @GetMapping("/default-time-slots")
    public ResponseEntity<List<DefaultTimeSlotResponse>> getDefaultTimeSlots() {
        List<DefaultTimeSlotResponse> slots = psychologistService.getDefaultTimeSlots();
        return ResponseEntity.ok(slots);
    }
}