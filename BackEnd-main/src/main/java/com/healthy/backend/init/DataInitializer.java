package com.healthy.backend.init;

import com.healthy.backend.dto.auth.request.RegisterRequest;

import com.healthy.backend.entity.*;
import com.healthy.backend.enums.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;
import com.healthy.backend.service.GeneralService;
import com.healthy.backend.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;

    private final UserLogRepository userLogRepository;
    private final AppointmentRepository appointmentRepository;
    private final DefaultTimeSlotRepository defaultTimeSlotRepository;
    private final NotificationRepository notificationRepository;
    private final CategoriesRepository categoriesRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyQuestionRepository surveyQuestionsRepository;
    private final GeneralService __;
    private final AuthenticationService authenticationService;
    private final SurveyService surveyService;

    private void initialize() {
        registerUsers();
        System.out.println("Users registered");
        initializeParentsAndStudents();
        System.out.println("Parents and Students initialized");
        initializeDepartments();
        System.out.println("Departments initialized");
        initializePsychologists();
        System.out.println("Psychologists initialized");
        initializeDefaultSlots();
        System.out.println("DefaultTimeSlots initialize");

        initializeCategories();
        System.out.println("Categories initialized");

        initializeLogs();
        System.out.println("Logs initialized");
        initializeNotifications();
        System.out.println("Notifications initialized");
    }

    private void registerUsers() {
        List<RegisterRequest> users = List.of(
                new RegisterRequest("adminpass", "Admin Admin", "admin@example.com", "1111111111", "Street 123, Ho Chi Minh City", Role.MANAGER.toString(), Gender.MALE.toString()),
                new RegisterRequest("staff_pass", "Staff Member", "staff@example.com", "2222222222", "Street 202, Ho Chi Minh City", Role.MANAGER.toString(), Gender.FEMALE.toString()),

                new RegisterRequest("psychologist_pass", "Dr. Brown", "psychologist@example.com", "0912345671", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
                new RegisterRequest("psychologist_pass", "Dr. Blue", "psychologist2@example.com", "0912345672", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),

                new RegisterRequest("parent_pass", "Jane Smith", "parent@example.com", "0812345671", "Street 789, Ho Chi Minh City", Role.PARENT.toString(), Gender.FEMALE.toString()),
                new RegisterRequest("parent_pass", "Bob Johnson", "parent2@example.com", "0812345672", "Street 404, Ho Chi Minh City", Role.PARENT.toString(), Gender.MALE.toString()),

                new RegisterRequest("student_pass", "John Doe", "student@example.com", "0512345671", "Street 456, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_pass", "John Green", "student2@example.com", "0512345672", "Street 606, Ho Chi Minh City", Role.STUDENT.toString(), Gender.MALE.toString()),
                new RegisterRequest("student_pass", "Alice Jones", "student3@example.com", "0512345673", "Street 303, Ho Chi Minh City", Role.STUDENT.toString(), Gender.FEMALE.toString())
//
//                new RegisterRequest("psychologist_pass", "Dr. Anh", "cuongcaoleanh@gmail.com", "0912345673", "Street 101, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString()),
//                new RegisterRequest("psychologist_pass", "Dr. Cuong", "caoleanhcuong78@gmail.com", "0912345674", "Street 505, Ho Chi Minh City", Role.PSYCHOLOGIST.toString(), Gender.MALE.toString())
        );
        users.forEach(authenticationService::register);
    }

    private void initializeParentsAndStudents() {
        // Initialize Parents
        parentRepository.save(new Parents("PRT001", userRepository.findByEmail("parent@example.com").getUserId()));
        parentRepository.save(new Parents("PRT002", userRepository.findByEmail("parent2@example.com").getUserId()));

        // Initialize Students
        studentRepository.save(new Students("STU001", userRepository.findByEmail("student@example.com").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 10, "A", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        studentRepository.save(new Students("STU002", userRepository.findByEmail("student2@example.com").getUserId(), parentRepository.findById("PRT002").get().getParentID(), 9, "B", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));
        studentRepository.save(new Students("STU003", userRepository.findByEmail("student3@example.com").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 9, "A", "Example High School", BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)));

    }

    private void initializeDepartments() {
        List<Department> departments = List.of(
                new Department("DPT001", "Child & Adolescent Psychology"),
                new Department("DPT002", "School Counseling"),
                new Department("DPT003", "Behavioral Therapy"),
                new Department("DPT004", "Trauma & Crisis Intervention"),
                new Department("DPT005", "Family & Parent Counseling"),
                new Department("DPT006", "Stress & Anxiety Management"),
                new Department("DPT007", "Depression & Mood Disorders"),
                new Department("DPT008", "Special Education Support"),
                new Department("DPT009", "Social Skills & Peer Relation"),
                new Department("DPT010", "Suicide Prevention & Intervention"),
                new Department("DPT011", "Digital Well-being Intervention")
        );
        departments.forEach(departmentRepository::save);
    }

    private void initializePsychologists() {
        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID003", 10, PsychologistStatus.ACTIVE, "DPT001"));
        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID004", 8, PsychologistStatus.ACTIVE, "DPT007"));
//        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID010", 2, PsychologistStatus.ACTIVE, "DPT002"));
//        psychologistRepository.save(new Psychologists(__.generatePsychologistID(), "UID011", 2, PsychologistStatus.ACTIVE, "DPT003"));

    }

    private void initializeDefaultSlots() {
        if (defaultTimeSlotRepository.count() == 0) {
            List<DefaultTimeSlot> slots = new ArrayList<>();

            // Morning slots 8:00-11:00
            LocalTime time = LocalTime.of(8, 0);
            for (int i = 0; time.isBefore(LocalTime.of(11, 0)); i++) {
                slots.add(new DefaultTimeSlot(
                        "MORNING-" + String.format("%02d", i),
                        time,
                        time.plusMinutes(30),
                        "Morning"
                ));
                time = time.plusMinutes(30);
            }

            // Afternoon slots 13:00-17:00
            time = LocalTime.of(13, 0);
            for (int i = 0; time.isBefore(LocalTime.of(17, 0)); i++) {
                slots.add(new DefaultTimeSlot(
                        "AFTERNOON-" + String.format("%02d", i),
                        time,
                        time.plusMinutes(30),
                        "Afternoon"
                ));
                time = time.plusMinutes(30);
            }

            defaultTimeSlotRepository.saveAll(slots);
        }
    }





    private void initializeCategories() {
        List<Categories> categories = Arrays.stream(SurveyCategory.values())
                .map(category -> new Categories(String.format("CAT%03d", category.ordinal() + 1), category))
                .collect(Collectors.toList());
        categoriesRepository.saveAll(categories);
    }












    private void initializeLogs() {
        userLogRepository.save(new UserLogs("LOG001", userRepository.findByEmail("psychologist@example.com").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("LOG002", userRepository.findByEmail("student2@example.com").getUserId(), "244.178.44.111"));
        userLogRepository.save(new UserLogs("LOG003", userRepository.findByEmail("psychologist@example.com").getUserId(), "38.0.101.76"));
        userLogRepository.save(new UserLogs("LOG004", userRepository.findByEmail("parent2@example.com").getUserId(), "89.0.142.86"));
    }



    private void initializeNotifications() {
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("psychologist@example.com").getUserId(), "Appointment Scheduled", "Your appointment is scheduled", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student@example.com").getUserId(), "New Appointment", "You have a new appointment", NotificationType.APPOINTMENT));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student@example.com").getUserId(), "New Survey", "You have a new survey", NotificationType.SURVEY));
        notificationRepository.save(new Notifications(__.generateNextNotificationID(), userRepository.findByEmail("student@example.com").getUserId(), "New Program", "You have a new program", NotificationType.PROGRAM));
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            this.initialize();
        }
    }
}
