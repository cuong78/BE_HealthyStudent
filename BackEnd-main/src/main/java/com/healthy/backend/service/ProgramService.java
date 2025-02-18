package com.healthy.backend.service;

import com.healthy.backend.dto.programs.ProgramParticipationRequest;
import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramParticipationRepository programParticipationRepository;

    private final StudentRepository studentRepository;

    private final ProgramScheduleRepository programScheduleRepository;

    private final TagsRepository tagsRepository;

    private final  DepartmentRepository departmentRepository;

    private final PsychologistRepository psychologistRepository;

    private final UserRepository userRepository;

    private final ProgramMapper programMapper;

    private final StudentMapper studentMapper;

    public List<ProgramsResponse> getAllProgramsDetails() {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(
                program -> programMapper.buildProgramsDetailsResponse(
                        program,
                        getStudentsByProgram(program.getProgramID())
                )).toList();
    }

    public List<ProgramsResponse> getAllPrograms() {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(programMapper::buildProgramResponse).toList();
    }

    public ProgramsResponse createProgram(ProgramsRequest programsRequest) {
        String programId = generateNextProgramId(programRepository.findLastProgramId());

        Optional<Users> staffUser = userRepository.findById(programsRequest.getUserId());
        if (staffUser.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + programsRequest.getUserId() + " not found.");
        }


        HashSet<Tags> tags = (HashSet<Tags>) programsRequest.getTags().stream()
                .map(tagName -> {
                    return tagsRepository.findByTagName(tagName)
                            .orElseGet(() -> {
                                Tags newTag = new Tags(generateNextTagId(tagsRepository.findLastTagId())
                                        , Tags.Tag.valueOf(tagName));
                                return tagsRepository.save(newTag);
                            });
                })
                .collect(Collectors.toSet());

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
                Programs.Status.valueOf(programsRequest.getStatus()),
                department,
                facilitator,
                tags,
                LocalDate.parse(programsRequest.getStartDate()),
                programsRequest.getMeetingLink(),
                Programs.Type.valueOf(programsRequest.getType())
        ));
        return getProgramById(programId);
    }

    public ProgramsResponse getProgramById(String programId) {
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        return programMapper.buildProgramResponse(program);
    }

    public boolean registerForProgram(ProgramParticipationRequest programParticipationRequest) {

        Programs program = programRepository.findById(programParticipationRequest.getProgramID())
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));

        if (isJoined(programParticipationRequest)) {
            throw new ResourceAlreadyExistsException("Student is already registered for this program");
        }
        String programParticipationId = generateNextProgramParticipationId(programParticipationRepository.findLastParticipationCode());
        programParticipationRepository.save(
                new ProgramParticipation(
                        programParticipationId,
                        programParticipationRequest.getStudentID(),
                        programParticipationRequest.getProgramID(),
                        ProgramParticipation.Status.Joined,
                        LocalDate.now()
                )
        );
        return programParticipationRepository.findById(programParticipationId).isPresent();
    }

    public String getProgramStatus(String programId) {
        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        return program.getStatus().name();
    }

    public boolean cancelParticipation(ProgramParticipationRequest programParticipationRequest) {
        if (!isJoined(programParticipationRequest)) {
            throw new ResourceNotFoundException("Participation not found");
        }
        ProgramParticipation participation = programParticipationRepository.findByProgramIDAndStudentID(
                programParticipationRequest.getProgramID(), programParticipationRequest.getStudentID());
        if (participation.getStatus().equals(ProgramParticipation.Status.Cancelled))
            throw new ResourceAlreadyExistsException("Participation is already cancelled");
        if (participation.getStatus().equals(ProgramParticipation.Status.Completed)) {
            throw new ResourceAlreadyExistsException("Participation is already completed");
        }
        participation.setStatus(ProgramParticipation.Status.Cancelled);
        ProgramParticipation updatedParticipation = programParticipationRepository.save(participation);
        return updatedParticipation.getStatus().equals(ProgramParticipation.Status.Cancelled);
    }

    public List<ProgramsResponse> getEnrolledPrograms(String studentId) {
        List<ProgramParticipation> participation = programParticipationRepository.findByStudentID(studentId);
        if (participation.isEmpty()) {
            throw new ResourceNotFoundException("No enrolled programs found");
        }
        return participation.stream()
                .map(p -> programMapper.buildProgramResponse(
                        programRepository
                                .findById(p.getProgram().getProgramID())
                                .orElseThrow(() -> new ResourceNotFoundException("Program not found"))
                ))
                .toList();
    }


    @Transactional
    public boolean deleteProgram(String programId) {
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found with ID: " + programId);
        }

        programParticipationRepository.deleteByProgramId(programId);

        programScheduleRepository.deleteByProgramId(programId);

        programRepository.deleteById(programId);

        if (programRepository.findById(programId).isPresent()) return false;
        return programRepository.findById(programId).isEmpty();
    }

    private List<StudentResponse> getStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findStudentIDsByProgramID(programId);

        if (studentIDs.isEmpty()) {
            return new ArrayList<>();
        }

        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .toList();
    }

    public List<ProgramParticipationResponse> getProgramParticipants(String programId) {

        List<ProgramParticipation> programParticipants = programParticipationRepository.findByProgramID(programId);

        if (programParticipants.isEmpty()) {
            throw new ResourceNotFoundException("No programs found");
        }

        return programParticipants.stream().map(
                programMapper::buildProgramParticipationResponse
        ).toList();
    }

    private boolean isJoined(ProgramParticipationRequest programParticipationRequest) {
        return programParticipationRepository.findByProgramIDAndStudentID(
                programParticipationRequest.getProgramID(), programParticipationRequest.getStudentID()
        ) != null;
    }


    private String generateNextTagId(String lastCode) {
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last tag code");
        }
        String prefix = lastCode.substring(0, 3);
        int number = Integer.parseInt(lastCode.substring(3));
        return prefix + String.format("%03d", number + 1);
    }

    private String generateNextProgramId(String lastCode) {
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last program code");
        }
        String prefix = lastCode.substring(0, 3);
        int number = Integer.parseInt(lastCode.substring(3));
        return prefix + String.format("%03d", number + 1);
    }

    private String generateNextProgramParticipationId(String lastCode) {
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last participation code");
        }
        String prefix = lastCode.substring(0, 2);
        int number = Integer.parseInt(lastCode.substring(2));
        return prefix + String.format("%03d", number + 1);
    }
}
