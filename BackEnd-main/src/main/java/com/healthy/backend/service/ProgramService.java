package com.healthy.backend.service;

import com.healthy.backend.dto.programs.*;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.ProgramStatus;
import com.healthy.backend.enums.ProgramType;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final ProgramMapper programMapper;
    private final StudentMapper studentMapper;

    private final GeneralService __;
    private final NotificationService notificationService;
    private final TokenService tokenService;

    public List<ProgramsResponse> getAllProgramsDetails(HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to view data for all programs");
        }
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(
                program -> programMapper.buildProgramsDetailsResponse(
                        program,
                        getActiveStudentsByProgram(program.getProgramID())
                )).toList();
    }

    public List<ProgramsResponse> getAllPrograms(HttpServletRequest request) {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(program -> {
            List<StudentResponse> enrolled = getActiveStudentsByProgram(program.getProgramID());
            return programMapper.buildProgramResponse(program, enrolled.size());
        }).toList();
    }

    public ProgramTagResponse createProgramTag(ProgramTagRequest programTagRequest, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to create this program tag");
        }
        if (tagsRepository.existsByTagName(programTagRequest.getTagName())) {
            throw new ResourceAlreadyExistsException("Tag already exists");
        }
        String tagId = __.generateTagID();
        Tags newTag = new Tags(tagId, programTagRequest.getTagName());
        tagsRepository.save(newTag);
        return programMapper.buildProgramTagResponse(newTag);
    }

    public ProgramsResponse createProgram(ProgramsRequest programsRequest, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to create this program");
        }
        String programId = __.generateProgramID();
        Optional<Users> staffUser = userRepository.findById(programsRequest.getUserId());
        if (staffUser.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + programsRequest.getUserId() + " not found.");
        }

        HashSet<Tags> tags = programsRequest.getTags()
                .stream()
                .map(String::toUpperCase)
                .map(tag -> tagsRepository.findById(tag)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + tag)))
                .collect(Collectors.toCollection(HashSet::new));

        Department department = departmentRepository.findById(programsRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + programsRequest.getDepartmentId()));
        Psychologists facilitator = psychologistRepository.findById(programsRequest.getFacilitatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + programsRequest.getFacilitatorId()));
        programRepository.save(new Programs(
                programId,
                programsRequest.getName(),
                programsRequest.getDescription(),
                programsRequest.getNumberParticipants(),
                programsRequest.getDuration(),
                ProgramStatus.valueOf(programsRequest.getStatus().toUpperCase()),
                department,
                facilitator,
                tags,
                LocalDate.parse(programsRequest.getStartDate()),
                programsRequest.getMeetingLink(),
                ProgramType.valueOf(programsRequest.getType().toUpperCase())
        ));
        return getProgramById(programId, request);
    }

    public ProgramsResponse getProgramById(String programId, HttpServletRequest request) {
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        return programMapper.buildProgramResponse(program, getActiveStudentsByProgram(programId).size());
    }

    public List<ProgramTagResponse> getProgramTags(HttpServletRequest request) {
        List<Tags> tags = tagsRepository.findAll();
        if (tags.isEmpty()) throw new ResourceNotFoundException("No tags found");
        return tags.stream().map(programMapper::buildProgramTagResponse).toList();
    }

    public boolean registerForProgram(String programId, HttpServletRequest request) {
        if (!tokenService.isStudent(request)) {
            throw new OperationFailedException("You don't have permission to register for a program");
        }
        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));

        String studentId = tokenService.getRoleID(tokenService.retrieveUser(request));
        if (isJoined(programId, studentId)) {
            throw new ResourceAlreadyExistsException("Student is already registered for this program");
        }
        String programParticipationId = __.generateParticipantID();
        programParticipationRepository.save(
                new ProgramParticipation(
                        programParticipationId,
                        studentId,
                        programId,
                        ParticipationStatus.JOINED,
                        LocalDate.now()
                )
        );

        // Send notification
        notificationService.createProgramNotification(
                studentRepository.findByStudentID(studentId).getUserID(),
                "New Program Registration",
                "You have a new program registration for " + program.getProgramName(),
                program.getProgramID());

        return programParticipationRepository.findById(programParticipationId).isPresent();
    }

    public String getProgramStatus(String programId, HttpServletRequest request) {
        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        return program.getStatus().name();
    }

    public boolean cancelParticipation(String programId, HttpServletRequest request) {
        String studentId = tokenService.getRoleID(tokenService.retrieveUser(request));
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(studentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to cancel this student participation");
        }
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found");
        }
        if (!isJoined(programId, studentId)) {
            throw new ResourceNotFoundException("Participation not found");
        }
        ProgramParticipation participation = programParticipationRepository.findByProgramIDAndStudentID(
                programId,
                studentId).getLast();
        if (participation.getStatus().equals(ParticipationStatus.CANCELLED))
            throw new ResourceAlreadyExistsException("Participation is already cancelled");

        if (participation.getStatus().equals(ParticipationStatus.COMPLETED)) {
            throw new ResourceAlreadyExistsException("Participation is already completed");
        }

        participation.setStatus(ParticipationStatus.CANCELLED);
        ProgramParticipation updatedParticipation = programParticipationRepository.save(participation);

        notificationService.createProgramNotification(
                participation.getStudent().getUser().getUserId(),
                "New Program Registration",
                "You have a new program registration for " + participation.getProgram().getProgramName(),
                participation.getProgram().getProgramID());

        return updatedParticipation.getStatus().equals(ParticipationStatus.CANCELLED);
    }

    public List<ProgramsResponse> getEnrolledPrograms(
            String studentId,
            HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student's enrolled programs");
        }
        List<ProgramParticipation> participationList = programParticipationRepository.findByStudentID(finalStudentId).stream()
                .filter(participation -> !participation.getStatus().equals(ParticipationStatus.CANCELLED))
                .toList();

        if (participationList.isEmpty()) {
            return new ArrayList<>();
        }

        return participationList.stream()
                .map(participation -> {
                    Programs program = participation.getProgram();
                    return programMapper.buildProgramResponse(program, getActiveStudentsByProgram(program.getProgramID()).size());
                }).toList();
    }

    @Transactional
    public boolean deleteProgram(String programId, HttpServletRequest request) {
        if (programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found");
        }
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to delete this program");
        }
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found with ID: " + programId);
        }

        Programs program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        program.setStatus(ProgramStatus.DELETED);
        programRepository.save(program);

        if (programRepository.findById(programId).isPresent()) return false;
        return programRepository.findById(programId).isEmpty();
    }

    public ProgramsResponse updateProgram(String programId, ProgramUpdateRequest updateRequest, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to update this program");
        }

        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");

        if (program.getStartDate().isBefore(LocalDate.now()) &&
                program.getStartDate().plusDays(program.getDuration()).isAfter(LocalDate.now())) {
            throw new OperationFailedException("Program is currently in progress and cannot be updated");
        }

        if (!program.getStartDate().isEqual(LocalDate.parse(updateRequest.getStartDate())) &&
                program.getStartDate().isBefore(LocalDate.now())) {
            throw new OperationFailedException("Cannot modify start date of a program that has already started");
        }

        LocalDate newStartDate = LocalDate.parse(updateRequest.getStartDate());
        LocalDate newEndDate = newStartDate.plusDays(updateRequest.getDuration());
        if (newEndDate.isBefore(LocalDate.now())) {
            throw new OperationFailedException("End date cannot be in the past");
        }

        int enrolledCount = programParticipationRepository.findByProgramID(programId).size();
        if (updateRequest.getNumberParticipants() < enrolledCount) {
            throw new OperationFailedException("Number of participants cannot be less than the number of enrolled participants (" + enrolledCount + ")");
        }

        if (!departmentRepository.existsById(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Department not found");
        }

        if (!psychologistRepository.existsById(updateRequest.getFacilitatorId())) {
            throw new OperationFailedException("Facilitator not found");
        }

        Psychologists facilitator = psychologistRepository.findById(updateRequest.getFacilitatorId()).orElse(null);
        if (facilitator != null && !facilitator.getDepartmentID().equals(updateRequest.getDepartmentId())) {
            throw new OperationFailedException("Facilitator does not belong to the specified department");
        }

        ProgramStatus currentStatus = program.getStatus();
        ProgramStatus newStatus = ProgramStatus.valueOf(updateRequest.getStatus().toUpperCase());
        if (currentStatus == ProgramStatus.COMPLETED && newStatus != ProgramStatus.COMPLETED) {
            throw new OperationFailedException("Cannot change status of a completed program");
        }

        if (currentStatus == ProgramStatus.DELETED) {
            throw new OperationFailedException("Cannot update a cancelled program");
        }

        if (updateRequest.getMeetingLink() != null && !updateRequest.getMeetingLink().matches("^(http|https)://.*$")) {
            throw new OperationFailedException("Invalid meeting link format");
        }

        // Update program details
        program.setProgramName(updateRequest.getName());
        program.setDescription(updateRequest.getDescription());
        program.setDuration(updateRequest.getDuration());
        program.setNumberParticipants(updateRequest.getNumberParticipants());
        program.setStatus(newStatus);
        program.setMeetingLink(updateRequest.getMeetingLink());
        program.setType(ProgramType.valueOf(updateRequest.getType().toUpperCase()));
        program.setFacilitatorID(updateRequest.getFacilitatorId());
        program.setDepartmentID(updateRequest.getDepartmentId());
        program.setStartDate(newStartDate);

        // Handle tags
        Set<Tags> tags = new HashSet<>();
        for (String tagId : updateRequest.getTags()) {
            Tags tag = tagsRepository.findById(tagId).orElse(null);
            if (tag == null) throw new ResourceNotFoundException("Tag not found");
            tags.add(tag);
        }
        program.setTags(tags);

        programRepository.save(program);
        return programMapper.buildProgramResponse(program, getActiveStudentsByProgram(programId).size());
    }


    public ProgramsResponse getProgramParticipants(String programId, HttpServletRequest request) {
        if (!tokenService.validateRoles(request, List.of(Role.MANAGER, Role.PSYCHOLOGIST))) {
            throw new OperationFailedException("You don't have permission to get participants of this program");
        }
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<StudentResponse> studentResponses = getActiveStudentsByProgram(programId)
                .stream()
                .filter(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId()).getLast();
                    return programParticipation != null && programParticipation.getStatus().equals(ParticipationStatus.JOINED);
                })
                .collect(Collectors.toList());
        if (studentResponses.isEmpty()) programMapper.buildProgramsParticipantResponse(program, new ArrayList<>());
        return programMapper.buildProgramsParticipantResponse(program, studentResponses);
    }

    private List<StudentResponse> getActiveStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findActiveStudentIDsByProgramID(programId, ParticipationStatus.CANCELLED);

        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId()).getLast();
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }

    private List<StudentResponse> getStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findStudentIDsByProgramID(programId);
        if (studentIDs.isEmpty()) {
            return new ArrayList<>();
        }
        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId()).getLast();
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }

    private boolean isJoined(String programId, String studentId) {
        ProgramParticipation participation =  programParticipationRepository.findByProgramIDAndStudentID(
                programId, studentId).getLast();
        return participation != null && participation.getStatus().equals(ParticipationStatus.JOINED);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStart() {
        System.out.println("Checking program statuses at startup...");
        updateProgramStatuses();
    }

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void scheduledStatusUpdate() {
        System.out.println("Checking program statuses at midnight...");
        updateProgramStatuses();
    }

    private void updateProgramStatuses() {
        LocalDate today = LocalDate.now();
        List<Programs> programs = programRepository.findAll();

        for (Programs program : programs) {
            LocalDate endDate = program.getStartDate().plusDays(program.getDuration());

            // Change PENDING → IN_PROGRESS
            if (program.getStatus() == ProgramStatus.ACTIVE && program.getStartDate().isBefore(today)) {
                program.setStatus(ProgramStatus.IN_PROGRESS);
            }

            // Change IN_PROGRESS → COMPLETED
            if (program.getStatus() == ProgramStatus.IN_PROGRESS && endDate.isBefore(today)) {
                program.setStatus(ProgramStatus.COMPLETED);
            }
        }

        programRepository.saveAll(programs); // Bulk update
        System.out.println("Program statuses updated: " + today);
    }

    private String validateStudentID(HttpServletRequest request, String studentId) {
        if (studentId == null) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        if (!studentRepository.existsById(studentId)) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        return studentId;
    }
}
