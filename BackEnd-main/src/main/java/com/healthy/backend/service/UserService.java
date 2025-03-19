package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final AppointmentRepository appointmentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final PsychologistRepository psychologistRepository;

    private final UserMapper userMapper;
    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
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
            case PSYCHOLOGIST -> {
                return getPsychologistDetails(user);
            }

            default -> throw new ResourceNotFoundException("User not found with id: " + userId);
        }
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
                surveyResultsResponseList,
                null);
    }


    private UsersResponse getPsychologistDetails(Users user) {
        return userMapper.buildUserDetailsResponse(
                user,
                psychologistsMapper.buildPsychologistResponse(psychologistRepository.findByUserID(user.getUserId())),
                null,
                getPsychologistAppointments(user.getUserId()),
                null,
                null);
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
                                        psychologistRepository.findByPsychologistID(
                                                appointment.getPsychologistID())),
                                null
                        ))
                .toList();
    }



    // Get user program participation


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