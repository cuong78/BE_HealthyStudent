package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.student.StudentRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final AppointmentRepository appointmentsRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final TokenService tokenService;

    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;
    private final AppointmentMapper appointmentMapper;
    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;

    public boolean isStudentExist(String studentId) {
        if (!studentRepository.existsById(studentId))
            throw new ResourceNotFoundException("No students found with id: " + studentId);
        return true;
    }

    // Get all students
    public List<StudentResponse> getAllStudents(HttpServletRequest request) {
        List<Students> students = studentRepository.findAll();
        if (students.isEmpty())
            throw new ResourceNotFoundException("The system have no students yet");
        return students
                .stream()
                .map(studentMapper::buildStudentResponse)
                .toList();
    }
    public String getStudentIdByUserId(String userId) {
        Students students = studentRepository.findByUserID(userId);
        if (students == null) {
            throw new ResourceNotFoundException("Psychologist not found for user");
        }

        return students.getStudentID();

    }

    // Get student by ID
    public StudentResponse getStudentById(String studentId, HttpServletRequest request) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id: " + studentId));
        return studentMapper.buildStudentResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(String studentId, StudentRequest student, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student programs");
        }
        Students existingStudent = studentRepository.findById(student.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id: " + student.getStudentID()));

        existingStudent.setGrade(student.getGrade());
        existingStudent.setClassName(student.getClassName());
        existingStudent.setSchoolName(student.getSchoolName());

        Users user = existingStudent.getUser();
        user.setFullName(student.getName());
        user.setEmail(student.getEmail());
        user.setPhoneNumber(student.getPhoneNumber());
        user.setAddress(student.getAddress());
        user.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(existingStudent);

        return studentMapper.buildStudentResponse(existingStudent);
    }

    public List<SurveysResponse> getSurvey(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student survey results");
        }
        List<Surveys> surveys = surveyRepository.findAll();
        if (surveys.isEmpty()) {
            throw new ResourceNotFoundException("No surveys found");
        }
        return surveys
                .stream()
                .map(surveyMapper::buildSurveysResponse).toList();
    }

    public List<SurveysResponse> getPendingSurveys(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student survey results");
        }
        List<SurveyResult> results = surveyResultRepository.findByStudentID(studentId);
        List<Surveys> surveys = surveyRepository.findAll();
        HashMap<String, String> surveyMap = new HashMap<>();
        for (Surveys survey : surveys) {
            surveyMap.put(survey.getSurveyID(), surveyResultRepository.existsBySurveyIDAndStudentID(survey.getSurveyID(), studentId) ? "COMPLETED" : "PENDING");
        }
        if (results.isEmpty()) {
            return List.of();
        }
        return surveys.stream()
                .map(sr -> {
                            SurveyResult surveyResult = surveyResultRepository.findBySurveyIDAndStudentID(sr.getSurveyID(), studentId);
                            return surveyMapper.buildSurveysResponse(
                                    sr,
                                    surveyQuestionRepository.findBySurveyID(sr.getSurveyID()).size(),
                                    surveyMap.get(sr.getSurveyID()),
                                    surveyResult.getResult() + "/" + surveyResult.getMaxScore());
                        }
                ).toList();
    }

    public List<ProgramsResponse> getEnrolledPrograms(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student programs");
        }
        return programParticipationRepository.findByStudentID(studentId).stream()
                .map(p -> programMapper.buildProgramResponse(
                        programRepository
                                .findById(p.getProgram().getProgramID())
                                .orElseThrow(() -> new ResourceNotFoundException("Program not found")),
                        getStudentsByProgram(p.getProgram().getProgramID()).size()
                )).toList();
    }

    public List<ProgramsResponse> getCompletedPrograms(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student programs");
        }
        List<ProgramParticipation> participation = programParticipationRepository.findByStudentID(studentId);
        if (participation.isEmpty()) {
            throw new ResourceNotFoundException("No enrolled programs found");
        }
        return participation.stream()
                .map(p -> programMapper.buildProgramResponse(
                        programRepository
                                .findById(p.getProgram().getProgramID())
                                .orElseThrow(() -> new ResourceNotFoundException("Program not found")),
                        getStudentsByProgram(p.getProgram().getProgramID()).size()
                ))
                .toList();
    }

    public List<AppointmentResponse> getAppointments(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student appointments");
        }
        List<Appointments> appointments = appointmentsRepository.findByStudentID(studentId);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .map(appointmentMapper::buildAppointmentResponse).toList();
    }

    public List<AppointmentResponse> getUpcomingAppointments(String studentId, HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.isManager(request)) {
            throw new OperationFailedException("You don't have permission to view this student appointments");
        }
        List<Appointments> appointments = appointmentsRepository.findByStudentID(studentId)
                .stream()
                .filter(appointment -> appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now()))
                .toList();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
                .map(appointmentMapper::buildAppointmentResponse).toList();
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