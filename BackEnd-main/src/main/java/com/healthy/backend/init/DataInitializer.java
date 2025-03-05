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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    private final ProgramScheduleRepository programScheduleRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final UserLogRepository userLogRepository;
    private final ArticleRepository articleRepository;
    private final AppointmentRepository appointmentRepository;
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
//
//        System.out.println("TimeSlots initialize");
        initializeTags();
        System.out.println("Tags initialized");
        initializePrograms();
        System.out.println("Programs initialized");
        initializeProgramSchedule();
        System.out.println("Program Schedule initialized");
        initializeCategories();
        System.out.println("Categories initialized");
        initializeSurveys();
        System.out.println("Surveys initialized");
        initializeSurveyQuestions();
        System.out.println("Survey Questions initialized");
        initializeAnswers();
        System.out.println("Answers initialized");
        initializeSurveyResults();
        System.out.println("Survey Results initialized");
        initializeSurveyResultsChoices();
        System.out.println("Score initialized");
        initScore();
        System.out.println("Survey Results Choices initialized");
        initializeLogs();
        System.out.println("Logs initialized");
        initializeArticles();
        System.out.println("Articles initialized");
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
        studentRepository.save(new Students("STU001", userRepository.findByEmail("student@example.com").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 10, "A", "Example High School", 0, 0, 0));
        studentRepository.save(new Students("STU002", userRepository.findByEmail("student2@example.com").getUserId(), parentRepository.findById("PRT002").get().getParentID(), 9, "B", "Example High School", 0, 0, 0));
        studentRepository.save(new Students("STU003", userRepository.findByEmail("student3@example.com").getUserId(), parentRepository.findById("PRT001").get().getParentID(), 9, "A", "Example High School", 0, 0, 0));

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



    private void initializeTags() {
        List<Tags> tags = Arrays.stream(ProgramTags.values())
                .map(tag -> new Tags(String.format("TAG%03d", tag.ordinal() + 1), tag))
                .collect(Collectors.toList());
        tagsRepository.saveAll(tags);
    }

    private void initializePrograms() {
        programRepository.save(new Programs("PRG001", "Stress Management",
                "Program to help manage stress", 20, 4, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT006").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG001", "TAG002", "TAG003"))),
                LocalDate.parse("2025-02-23"), "https://example.com/meeting1", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG002", "Anxiety Support Group",
                "Support group for individuals with anxiety", 15, 6, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT006").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG004", "TAG005", "TAG006"))),
                LocalDate.parse("2025-02-25"), "https://example.com/meeting2", ProgramType.OFFLINE));
        programRepository.save(new Programs("PRG003", "Mindfulness Workshop",
                "Workshop on mindfulness techniques", 25, 3, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT003").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG007", "TAG008", "TAG009"))),
                LocalDate.parse("2025-02-28"), "https://example.com/meeting3", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG004", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-01"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG005", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-12"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG006", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-22"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG007", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-03-28"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG008", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-20"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG009", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-22"), "https://example.com/meeting4", ProgramType.ONLINE));
        programRepository.save(new Programs("PRG010", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, ProgramStatus.ACTIVE,
                departmentRepository.findById("DPT007").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-04-28"), "https://example.com/meeting4", ProgramType.ONLINE));

    }

    private void initializeProgramSchedule() {
        // Initialize Program Schedule
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG001", "Monday", LocalTime.parse("10:00:00"), LocalTime.parse("11:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG002", "Tuesday", LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG003", "Wednesday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG004", "Friday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG005", "Saturday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG006", "Wednesday", LocalTime.parse("07:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG007", "Tuesday", LocalTime.parse("08:30:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG008", "Monday", LocalTime.parse("07:00:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG009", "Tuesday", LocalTime.parse("09:30:00"), LocalTime.parse("10:30:00")));
        programScheduleRepository.save(new ProgramSchedule(__.generateProgramScheduleID(), "PRG010", "Monday", LocalTime.parse("09:30:00"), LocalTime.parse("10:30:00")));
    }

    private void initializeCategories() {
        List<Categories> categories = Arrays.stream(SurveyCategory.values())
                .map(category -> new Categories(String.format("CAT%03d", category.ordinal() + 1), category))
                .collect(Collectors.toList());
        categoriesRepository.saveAll(categories);
    }

    private void initializeSurveys() {
        surveyRepository.save(new Surveys("SUV001", "Stress Survey", "Survey to assess stress levels", "CAT001", userRepository.findByEmail("psychologist@example.com").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV002", "Anxiety Assessment", "Assessment of anxiety symptoms", "CAT002", userRepository.findByEmail("psychologist2@example.com").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV003", "Depression Screening", "Screening for depression", "CAT003", userRepository.findByEmail("psychologist2@example.com").getUserId(), SurveyStatus.ACTIVE));
        surveyRepository.save(new Surveys("SUV004", "Mood Assessment", "Assessment of mood", "CAT001", userRepository.findByEmail("psychologist@example.com").getUserId(), SurveyStatus.INACTIVE));
    }

    private void initializeSurveyQuestions() {
        surveyQuestionsRepository.save(new SurveyQuestions("SQR001", "SUV001", "In the last month, how often have you been upset because something that happened was unexpected?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR002", "SUV001", "In the last month, how often have you felt that you were unable to control the important things in your life?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR003", "SUV001", "In the last month, how often have you felt nervous and stressed?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR004", "SUV001", "In the last month, how often have you felt confident about your ability to handle your personal problems?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR005", "SUV001", "In the last month, how often have you felt that things were going your way?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR006", "SUV001", "In the last month, how often have you found that you could not cope with all the things you had to do?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR007", "SUV001", "In the last month, how often have you been able to control irritations in your life?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR008", "SUV001", "In the last month, how often have you felt that you were on top of things?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR009", "SUV001", "In the last month, how often have you been angered because of things that happened that were outside of your control?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR010", "SUV001", "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?", "CAT001"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR011", "SUV002", "Feeling nervous, anxious, or on edge?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR012", "SUV002", "Not being able to stop or control worrying?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR013", "SUV002", "Trouble relaxing", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR014", "SUV002", "Do you feel sad or hopeless most of the time?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR015", "SUV002", "Being so restless that it's hard to sit still?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR016", "SUV002", "Becoming easily annoyed or irritable?", "CAT002"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR017", "SUV002", "Feeling afraid as if something awful might happen?", "CAT002"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR018", "SUV003", "Little interest or pleasure in doing things?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR019", "SUV003", "Feeling down, depressed, or hopeless?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR020", "SUV003", "Trouble falling or staying asleep, or sleeping too much?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR021", "SUV003", "Feeling tired or having little energy?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR022", "SUV003", "Poor appetite or overeating?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR023", "SUV003", "Feeling bad about yourself — or that you are a failure or have let yourself or your family down?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR024", "SUV003", "Trouble concentrating on things, such as reading the newspaper or watching television?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR025", "SUV003", "Moving or speaking so slowly that other people could have noticed? Or so fidgety or restless that you have been moving a lot more than usual?", "CAT003"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR026", "SUV003", "Thoughts that you would be better off dead, or thoughts of hurting yourself in some way?", "CAT003"));

        surveyQuestionsRepository.save(new SurveyQuestions("SQR027", "SUV004", "How are you feeling today?", "CAT001"));
        surveyQuestionsRepository.save(new SurveyQuestions("SQR028", "SUV004", "Are you having a good day?", "CAT001"));

    }

    private void initializeAnswers() {
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO001", "SQR001", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO002", "SQR001", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO003", "SQR001", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO004", "SQR001", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO005", "SQR002", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO006", "SQR002", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO007", "SQR002", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO008", "SQR002", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO009", "SQR003", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO010", "SQR003", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO011", "SQR003", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO012", "SQR003", "Fairly Often", 3));


        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO013", "SQR004", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO014", "SQR004", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO015", "SQR004", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO016", "SQR004", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO017", "SQR005", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO018", "SQR005", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO019", "SQR005", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO020", "SQR005", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO021", "SQR006", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO022", "SQR006", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO023", "SQR006", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO024", "SQR006", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO025", "SQR007", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO026", "SQR007", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO027", "SQR007", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO028", "SQR007", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO029", "SQR008", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO030", "SQR008", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO031", "SQR008", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO032", "SQR008", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO033", "SQR009", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO034", "SQR009", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO035", "SQR009", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO036", "SQR009", "Fairly Often", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO037", "SQR010", "Never", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO038", "SQR010", "Almost Never", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO039", "SQR010", "Sometimes", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO040", "SQR010", "Fairly Often", 3));


        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO041", "SQR011", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO042", "SQR011", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO043", "SQR011", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO044", "SQR011", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO045", "SQR012", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO046", "SQR012", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO047", "SQR012", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO048", "SQR012", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO049", "SQR013", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO050", "SQR013", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO051", "SQR013", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO052", "SQR013", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO053", "SQR014", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO054", "SQR014", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO055", "SQR014", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO056", "SQR014", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO057", "SQR015", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO058", "SQR015", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO059", "SQR015", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO060", "SQR015", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO061", "SQR016", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO062", "SQR016", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO063", "SQR016", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO064", "SQR016", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO065", "SQR017", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO066", "SQR017", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO067", "SQR017", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO068", "SQR017", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO069", "SQR018", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO070", "SQR018", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO071", "SQR018", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO072", "SQR018", "Nearly every day", 3));


        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO073", "SQR019", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO074", "SQR019", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO075", "SQR019", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO076", "SQR019", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO077", "SQR020", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO078", "SQR020", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO079", "SQR020", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO080", "SQR020", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO081", "SQR021", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO082", "SQR021", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO083", "SQR021", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO084", "SQR021", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO085", "SQR022", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO086", "SQR022", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO087", "SQR022", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO088", "SQR022", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO089", "SQR023", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO090", "SQR023", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO091", "SQR023", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO092", "SQR023", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO093", "SQR024", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO094", "SQR024", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO095", "SQR024", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO096", "SQR024", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO097", "SQR025", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO098", "SQR025", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO099", "SQR025", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO100", "SQR025", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO101", "SQR026", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO102", "SQR026", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO103", "SQR026", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO104", "SQR026", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO105", "SQR027", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO106", "SQR027", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO107", "SQR027", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO108", "SQR027", "Nearly every day", 3));

        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO109", "SQR028", "Not at all", 0));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO110", "SQR028", "Several days", 1));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO111", "SQR028", "More than half the days", 2));
        surveyQuestionOptionsRepository.save(new SurveyQuestionOptions("SQO112", "SQR028", "Nearly every day", 3));



    }

    private void initializeSurveyResults() {
        surveyResultRepository.save(new SurveyResult("SRS001", "SUV001", "STU001"));
        surveyResultRepository.save(new SurveyResult("SRS002", "SUV002", "STU001"));
        surveyResultRepository.save(new SurveyResult("SRS003", "SUV003", "STU001"));

        surveyResultRepository.save(new SurveyResult("SRS004", "SUV001", "STU002"));
        surveyResultRepository.save(new SurveyResult("SRS005", "SUV002", "STU002"));
        surveyResultRepository.save(new SurveyResult("SRS006", "SUV003", "STU002"));

        surveyResultRepository.save(new SurveyResult("SRS007", "SUV001", "STU003"));
        surveyResultRepository.save(new SurveyResult("SRS008", "SUV002", "STU003"));
        surveyResultRepository.save(new SurveyResult("SRS009", "SUV003", "STU003"));
    }

    private void initializeSurveyResultsChoices() {
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR001", "SQO002"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR002", "SQO006"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR003", "SQO010"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR004", "SQO014"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR005", "SQO018"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR006", "SQO022"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR007", "SQO026"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR008", "SQO030"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR009", "SQO034"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS001", "SQR010", "SQO038"));

        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR011", "SQO042"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR012", "SQO046"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR013", "SQO050"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR014", "SQO054"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR015", "SQO058"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR016", "SQO062"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS002", "SQR017", "SQO066"));


        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR018", "SQO070"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR019", "SQO074"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR020", "SQO078"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR021", "SQO082"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR022", "SQO086"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR023", "SQO090"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR024", "SQO094"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR025", "SQO098"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS003", "SQR026", "SQO102"));


        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR001", "SQO001"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR002", "SQO005"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR003", "SQO009"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR004", "SQO013"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR005", "SQO017"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR006", "SQO021"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR007", "SQO025"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR008", "SQO029"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR009", "SQO033"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS004", "SQR010", "SQO037"));

        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR011", "SQO041"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR012", "SQO045"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR013", "SQO049"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR014", "SQO053"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR015", "SQO057"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR016", "SQO061"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS005", "SQR017", "SQO065"));

        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR018", "SQO069"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR019", "SQO073"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR020", "SQO077"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR021", "SQO081"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR022", "SQO085"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR023", "SQO089"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR024", "SQO093"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR025", "SQO097"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS006", "SQR026", "SQO101"));


        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR001", "SQO003"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR002", "SQO007"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR003", "SQO011"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR004", "SQO015"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR005", "SQO019"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR006", "SQO023"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR007", "SQO027"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR008", "SQO031"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR009", "SQO035"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS007", "SQR010", "SQO039"));


        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR011", "SQO043"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR012", "SQO047"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR013", "SQO051"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR014", "SQO055"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR015", "SQO059"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR016", "SQO063"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS008", "SQR017", "SQO067"));


        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR018", "SQO071"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR019", "SQO075"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR020", "SQO079"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR021", "SQO083"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR022", "SQO087"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR023", "SQO091"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR024", "SQO095"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR025", "SQO099"));
        surveyQuestionOptionsChoicesRepository.save(new SurveyQuestionOptionsChoices("SRS009", "SQR026", "SQO103"));

    }

    private void initScore() {
        List<SurveyResult> surveyResults = surveyResultRepository.findAll();
        surveyResults.forEach(
                result ->{
                    result.setMaxScore(surveyService.calculateMaxScore(result.getSurvey()));
                    result.setResult(surveyService.calculateTotalScore(result.getResultID()));
                }
        );
        surveyResultRepository.saveAll(surveyResults);
    }

    private void initializeLogs() {
        userLogRepository.save(new UserLogs("LoG001", userRepository.findByEmail("psychologist@example.com").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("LOG002", userRepository.findByEmail("student2@example.com").getUserId(), "244.178.44.111"));
        userLogRepository.save(new UserLogs("LOG003", userRepository.findByEmail("psychologist@example.com").getUserId(), "38.0.101.76"));
        userLogRepository.save(new UserLogs("LOG004", userRepository.findByEmail("parent2@example.com").getUserId(), "89.0.142.86"));
    }

    private void initializeArticles() {
        articleRepository.save(new Article("ATC001", "Managing Stress", userRepository.findByEmail("psychologist@example.com").getUserId(), "Tips for managing stress..."));
        articleRepository.save(new Article("ATC002", "Anxiety Management", userRepository.findByEmail("psychologist2@example.com").getUserId(), "Tips for managing anxiety..."));
        articleRepository.save(new Article("ATC003", "Depression Management", userRepository.findByEmail("psychologist2@example.com").getUserId(), "Tips for managing depression..."));
        articleRepository.save(new Article("ATC004", "Sleep Disorders", userRepository.findByEmail("psychologist@example.com").getUserId(), "Tips for managing sleep disorders..."));
        articleRepository.save(new Article("ATC005", "Eating Disorders", userRepository.findByEmail("psychologist2@example.com").getUserId(), "Tips for managing eating disorders..."));
        articleRepository.save(new Article("ATC006", "Addiction Management", userRepository.findByEmail("psychologist@example.com").getUserId(), "Tips for managing addiction..."));
        articleRepository.save(new Article("ATC007", "Anxiety and Depression Management", userRepository.findByEmail("psychologist@example.com").getUserId(), "Tips for managing anxiety..."));
        articleRepository.save(new Article("ATC008", "Stress Management", userRepository.findByEmail("psychologist2@example.com").getUserId(), "Tips for managing stress and anxiety ..."));
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
