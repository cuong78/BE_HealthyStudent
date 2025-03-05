package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final AppointmentRepository appointmentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final PsychologistRepository psychologistRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;
    private final AppointmentMapper appointmentMapper;
    private final PsychologistsMapper psychologistsMapper;


    public List<UsersResponse> getAllUsers() {
        if (isDatabaseEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(userMapper::buildBasicUserResponse)
                .toList();
    }

    public UsersResponse getUserById(String userId) {
        return userMapper.buildBasicUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId)));
    }

    public UsersResponse getUserDetailsById(String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        switch (user.getRole()) {
            case STUDENT -> {
                return getStudentDetails(user);
            }
            case PARENT -> {
                return getParentDetails(user);
            }
            case PSYCHOLOGIST -> {
                return getPsychologistDetails(user);
            }
            case MANAGER -> {
                break;
            }
            default -> throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return new UsersResponse();
    }

    public UsersResponse updateUser(String userId, Users updatedUser) {

        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        boolean hasChanges = false;

        if (!existingUser.getFullName().equals(updatedUser.getFullName())) {
            existingUser.setFullName(updatedUser.getFullName());
            hasChanges = true;
        }
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
            hasChanges = true;
        }
        if (!existingUser.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        return userMapper.buildBasicUserResponse(existingUser);
    }

    private UsersResponse getStudentDetails(Users user) {
        List<SurveyResultsResponse> surveyResultsResponseList = getUserSurveyResults(user.getUserId());
        return userMapper.buildUserDetailsResponse(
                user,
                null,
                studentMapper.buildStudentResponse(studentRepository
                        .findByUserID(user.getUserId()), surveyResultsResponseList),
                getStudentAppointments(user.getUserId()),
                getUserProgramParticipation(user.getUserId()),
                surveyResultsResponseList,
                null);
    }

    private UsersResponse getPsychologistDetails(Users user) {
        return userMapper.buildUserDetailsResponse(
                user,
                psychologistsMapper.buildPsychologistResponse(psychologistRepository.findByUserID(user.getUserId())),
                null,
                getPsychologistAppointments(user.getUserId()),
                getUserProgramFacility(user.getUserId()),
                null,
                null);
    }

    private UsersResponse getParentDetails(Users user) {
        Parents parent = parentRepository.findByUserID(user.getUserId());
        return userMapper.buildUserDetailsResponse(
                user,
                null,
                null,
                null,
                null,
                null,
                parent.getStudents().stream()
                        .map(studentMapper::buildStudentResponse)
                        .peek(student -> student.setUsersResponse(getStudentDetails(studentRepository.findByUserID(student.getUserId()).getUser())))
                        .toList());
    }

    public EventResponse getAllEvents(String userId) {
        Users users = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Appointments> appointments = List.of();
        List<Programs> programs = List.of();

        if (users.getRole().equals(Role.STUDENT)) {
            String studentID = studentRepository.findByUserID(userId).getStudentID();
            appointments = appointmentRepository.findByStudentID(studentID);
            programs = programParticipationRepository.findByStudentID(studentID)
                    .stream()
                    .map(participation -> programRepository
                            .findById(participation.getProgram().getProgramID())
                            .orElseThrow(() -> new ResourceNotFoundException("Program not found")))
                    .toList();
        }
        if (users.getRole().equals(Role.PSYCHOLOGIST)) {
            String psychologistID = psychologistRepository.findByUserID(userId).getPsychologistID();
            appointments = appointmentRepository.findByPsychologistID(psychologistID);
            programs = programRepository.findByFacilitatorID(psychologistID);
        }
        if (users.getRole().equals(Role.MANAGER)) {
            appointments = appointmentRepository.findAll();
            programs = programRepository.findAll();
        }
        appointments = appointments.stream()
                .filter(appointment -> appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now().minusDays(1)))
                .toList();

        programs = programs.stream()
                .filter(program -> program.getStartDate().isAfter(LocalDate.now().minusDays(1))).toList();

        if (appointments.isEmpty() && programs.isEmpty()) {
            return eventMapper.buildEmptyEventResponse(appointments, programs, userId);
        }

        return switch (users.getRole()) {
            case STUDENT -> eventMapper.buildStudentEventResponse(appointments, programs, userId);
            case PSYCHOLOGIST -> eventMapper.buildPsychologistEventResponse(appointments, programs, userId);
            case MANAGER -> eventMapper.buildManagerEventResponse(appointments, programs, userId);
            default -> throw new ResourceNotFoundException("User not found with id: " + userId);
        };
    }

    public UsersResponse deactivateUser(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public UsersResponse reactivateUser(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public UsersResponse updateUserRole(String userId, String role) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public String exportUserData(String userId, String format) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return "Exporting user data for " + user.getUserId() + " in format: " + format;
    }

    public List<UsersResponse> searchUsers(String name) {
        return userRepository.findByFullNameContaining(name).stream()
                .map(userMapper::buildBasicUserResponse)
                .toList();
    }

    public boolean deleteUser(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (user.isDeleted()) {
            return false;
        }
        user.setDeleted(false);
        userRepository.save(user);
        return true;
    }

    public List<AppointmentResponse> getUserAppointment(String userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (users.getRole().equals(Role.STUDENT)) {
            return this.getStudentAppointments(userId);
        }
        if (users.getRole().equals(Role.PSYCHOLOGIST)) {
            return this.getPsychologistAppointments(userId);
        }
        if (users.getRole().equals(Role.MANAGER)) {
            return appointmentRepository.findAll().stream()
                    .map(appointmentMapper::buildAppointmentResponse)
                    .toList();
        }
        return null;
    }

    private List<AppointmentResponse> getPsychologistAppointments(String userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        List<Appointments> appointmentsList = null;

        if (!user.getRole().equals(Role.PSYCHOLOGIST)) {
            return null;
        }

        appointmentsList = appointmentRepository.findByPsychologistID(
                psychologistRepository.findByUserID(userId).getPsychologistID()
        );

        return appointmentsList != null ? appointmentsList.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                null,
                                studentMapper.buildStudentResponse(
                                        studentRepository.findByUserID(
                                                appointment.getStudentID()))
                        ))
                .toList() : null;
    }

    private List<AppointmentResponse> getStudentAppointments(String userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        if (!user.getRole().equals(Role.STUDENT)) {
            return null;
        }
        return appointmentRepository
                .findByStudentID(studentRepository.findByUserID(userId).getStudentID())
                .stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistsMapper.buildPsychologistResponse(
                                        psychologistRepository.findByUserID(
                                                appointment.getPsychologistID())),
                                null
                        ))
                .toList();
    }

    private List<ProgramsResponse> getUserProgramFacility(String id) {
        return programRepository.findByFacilitatorID(id)
                .stream()
                .map(programs -> programMapper.buildProgramsDetailsResponse(programs,
                        programParticipationRepository.findStudentIDsByProgramID(
                                        programs.getProgramID()).stream()
                                .map(studentRepository::findByStudentID)
                                .map(studentMapper::buildStudentResponse)
                                .peek(sr -> {
                                    ProgramParticipation programParticipation = programParticipationRepository
                                            .findByProgramIDAndStudentID(programs.getProgramID(), sr.getStudentId()).getLast();
                                    if (programParticipation != null) {
                                        sr.setProgramStatus(programParticipation.getStatus().name());
                                    }
                                })
                                .toList()
                )).toList();
    }

    // Get user program participation
    private List<ProgramsResponse> getUserProgramParticipation(String userId) {
        String student = studentRepository.findByUserID(userId).getStudentID();
        List<ProgramParticipation> participation = programParticipationRepository.findByStudentID(student);
        // Construct responses
        return participation.stream()
                .map(programParticipation -> programMapper.buildProgramResponse(
                        programParticipation.getProgram(),
                        programParticipationRepository.findStudentIDsByProgramID(
                                programParticipation.getProgramID()).size()
                ))
                .toList();
    }

    // Check if database is empty
    private boolean isDatabaseEmpty() {
        return userRepository.count() == 0;
    }

    // Get user survey results
    private List<SurveyResultsResponse> getUserSurveyResults(String id) {
        List<SurveyResult> surveyResults = surveyResultRepository.findByStudentID(id);
        return surveyMapper.getUserSurveyResults(List.of()); //TEMP
    }
}