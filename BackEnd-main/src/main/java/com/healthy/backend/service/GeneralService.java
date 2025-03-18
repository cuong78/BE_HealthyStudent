package com.healthy.backend.service;

import com.healthy.backend.enums.Identifier;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralService {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final UserLogRepository userLogRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final PsychologistRepository psychologistRepository;

    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyResultRepository surveyResultRepository;






    public String generateDepartmentID() {
        String lastCode = departmentRepository.findLastDepartmentId();
        return generateNextId(Identifier.DPT.toString(), lastCode);
    }

    public String generateUserLogID() {
        String lastCode = userLogRepository.findLastUserLogId();
        return generateNextId(Identifier.LOG.toString(), lastCode);
    }



    public String generateSurveyID() {
        String lastCode = surveyRepository.findLastSurveyId();
        return generateNextId(Identifier.SUV.toString(), lastCode);
    }



    public String generateUserID() {
        String lastCode = userRepository.findLastUserId();
        return generateNextId(Identifier.UID.toString(), lastCode);
    }

    public String generateParentID() {
        String lastCode = parentRepository.findLastParentId();
        return generateNextId(Identifier.PAR.toString(), lastCode);
    }

    public String generateStudentID() {
        String lastCode = studentRepository.findLastStudentId();
        return generateNextId(Identifier.STU.toString(), lastCode);
    }

    public String generatePsychologistID() {
        String lastCode = psychologistRepository.findLastPsychologistId();
        return generateNextId(Identifier.PSY.toString(), lastCode);
    }



    public String generateNextNotificationID() {
        String lastCode = notificationRepository.findLastNotificationId();
        return generateNextId(Identifier.NOT.toString(), lastCode);
    }

    public String generateAppointmentId() {
        String lastCode = appointmentRepository.findLastAppointmentId();
        return generateNextId(Identifier.APP.toString(), lastCode);
    }

    public String generateQuestionOptionId() {
        String lastCode = surveyQuestionOptionsRepository.findLastQuestionOptionId();
        return generateNextId(Identifier.SQO.toString(), lastCode);
    }

    public String generateSurveyQuestionId() {
        String lastCode = surveyQuestionRepository.findLastQuestionId();
        return generateNextId(Identifier.SQR.toString(), lastCode);
    }

    public String generateSurveyResultId() {
        String lastCode = surveyResultRepository.findLastResultId();
        return generateNextId(Identifier.SRS.toString(), lastCode);
    }

    private String generateNextId(String prefix, String lastId) {
        int nextNumber = lastId == null ? 1 : parseId(lastId) + 1;
        return prefix + String.format("%03d", nextNumber);
    }


    private int parseId(String lastId) {
        try {
            return Integer.parseInt(lastId.replaceAll("[^0-9]", "")); // Extracts only numeric part
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid last ID format: " + lastId);
        }
    }
}
