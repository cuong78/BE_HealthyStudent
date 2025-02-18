package com.healthy.backend.init;

import com.healthy.backend.dto.auth.RegisterRequest;
import com.healthy.backend.entity.*;
import com.healthy.backend.entity.Enum.StatusEnum;
import com.healthy.backend.repository.*;
import com.healthy.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Service
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final AppointmentRepository appointmentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final BlogRepository blogRepository;
    private final NotificationRepository notificationRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PsychologistRepository psychologistRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ProgramParticipationRepository programParticipationRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final AnswersRepository answersRepository;
    private final UserLogRepository userLogRepository;
    private final ProgramScheduleRepository programScheduleRepository;
    private final CategoriesRepository categoryRepository;
    private final TagsRepository tagsRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthenticationService authenticationService;

    private void initialize() {

        // Initialize Users
        authenticationService.register(new RegisterRequest("admin", "adminpass", "Admin Admin", "admin@example.com", "1111111111", "Street 123, Ho Chi Minh City",Users.UserRole.MANAGER.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("staff_member", "staff_pass", "Staff Member", "staff@example.com", "2222222222", "Street 202, Ho Chi Minh City",Users.UserRole.MANAGER.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("student_user", "student_pass", "John Doe", "student@example.com", "3333333333","Street 456, Ho Chi Minh City", Users.UserRole.STUDENT.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("student_user2", "student_pass", "John Green", "student2@example.com", "4444444444", "Street 606, Ho Chi Minh City",Users.UserRole.STUDENT.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("student_user3", "student_pass", "Alice Jones", "student3@example.com", "5555555555", "Street 303, Ho Chi Minh City",Users.UserRole.STUDENT.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("parent_user", "parent_pass", "Jane Smith", "parent@example.com", "6666666666", "Street 789, Ho Chi Minh City",Users.UserRole.PARENT.toString(), Users.Gender.Female.toString()));
        authenticationService.register(new RegisterRequest("parent_user2", "parent_pass", "Bob Johnson", "parent2@example.com", "7777777777","Street 404, Ho Chi Minh City", Users.UserRole.PARENT.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("psychologist_user", "psychologist_pass", "Dr. Brown", "psychologist@example.com", "8888888888", "Street 101, Ho Chi Minh City", Users.UserRole.PSYCHOLOGIST.toString(), Users.Gender.Male.toString()));
        authenticationService.register(new RegisterRequest("psychologist_user2", "psychologist_pass", "Dr. Blue", "psychologist2@example.com", "9999999999","Street 505, Ho Chi Minh City", Users.UserRole.PSYCHOLOGIST.toString(), Users.Gender.Male.toString()));

        // Initialize Parents
        parentRepository.save(new Parents("P001", userRepository.findByUsername("parent_user").getUserId()));
        parentRepository.save(new Parents("P002", userRepository.findByUsername("parent_user2").getUserId()));

        // Initialize Students
        studentRepository.save(new Students("S001", userRepository.findByUsername("student_user").getUserId(), parentRepository.findById("P001").get().getParentID(), 10, "A", "Example High School", 5, 10, 2));
        studentRepository.save(new Students("S002", userRepository.findByUsername("student_user2").getUserId(), parentRepository.findById("P002").get().getParentID(), 9, "B", "Example High School", 3, 4, 7));
        studentRepository.save(new Students("S003", userRepository.findByUsername("student_user3").getUserId(), parentRepository.findById("P001").get().getParentID(), 9, "A", "Example High School", 2, 2, 1));

        // Initialize Department
        departmentRepository.save(new Department("DP01", "Child & Adolescent Psychology"));
        departmentRepository.save(new Department("DP02", "School Counseling"));
        departmentRepository.save(new Department("DP03", "Behavioral Therapy"));
        departmentRepository.save(new Department("DP04", "Trauma & Crisis Intervention"));
        departmentRepository.save(new Department("DP05", "Family & Parent Counseling"));
        departmentRepository.save(new Department("DP06", "Stress & Anxiety Management"));
        departmentRepository.save(new Department("DP07", "Depression & Mood Disorders"));
        departmentRepository.save(new Department("DP08", "Special Education Support"));
        departmentRepository.save(new Department("DP09", "Social Skills & Peer Relation"));
        departmentRepository.save(new Department("DP10", "Suicide Prevention & Intervention"));
        departmentRepository.save(new Department("DP11", "Digital Well-beingIntervention"));

        // Initialize Psychologists
        psychologistRepository.save(new Psychologists("PSY001", userRepository.findByUsername("psychologist_user").getUserId(), 10, Psychologists.Status.Active,"DP01"));
        psychologistRepository.save(new Psychologists("PSY002", userRepository.findByUsername("psychologist_user2").getUserId(), 8, Psychologists.Status.Active,"DP02"));

        // Initialize Time Slots
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),1));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),2));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),3));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),4));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),5));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),6));

        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),1));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),2));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),3));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),4));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),5));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-20"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),6));

        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),1));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),2));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),3));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),4));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"),psychologistRepository.findById("PSY001").orElseThrow(),5));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"),psychologistRepository.findById("PSY001").orElseThrow(),6));

        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("08:00:00"), LocalTime.parse("08:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),1));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("08:30:00"), LocalTime.parse("09:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),2));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("09:00:00"), LocalTime.parse("09:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),3));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("09:30:00"), LocalTime.parse("10:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),4));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("10:00:00"), LocalTime.parse("10:30:00"),psychologistRepository.findById("PSY002").orElseThrow(),5));
        timeSlotRepository.save(new TimeSlots(LocalDate.parse("2025-02-21"), LocalTime.parse("10:30:00"), LocalTime.parse("11:00:00"),psychologistRepository.findById("PSY002").orElseThrow(),6));

        // Initialize Tags
        tagsRepository.save(new Tags("TAG001", Tags.Tag.Stress));
        tagsRepository.save(new Tags("TAG002", Tags.Tag.Anxiety));
        tagsRepository.save(new Tags("TAG003", Tags.Tag.Mindfulness));
        tagsRepository.save(new Tags("TAG004", Tags.Tag.Depression));
        tagsRepository.save(new Tags("TAG005", Tags.Tag.EatingDisorder));
        tagsRepository.save(new Tags("TAG006", Tags.Tag.Addiction));
        tagsRepository.save(new Tags("TAG007", Tags.Tag.Self_Care));
        tagsRepository.save(new Tags("TAG008", Tags.Tag.Sleep));
        tagsRepository.save(new Tags("TAG009", Tags.Tag.PhysicalHealth));
        tagsRepository.save(new Tags("TAG010", Tags.Tag.MentalHealth));
        tagsRepository.save(new Tags("TAG011", Tags.Tag.Peer_Support));
        tagsRepository.save(new Tags("TAG012", Tags.Tag.Community));
        tagsRepository.save(new Tags("TAG013", Tags.Tag.Social_Skill));
        tagsRepository.save(new Tags("TAG014", Tags.Tag.Exercise));
        tagsRepository.save(new Tags("TAG015", Tags.Tag.Relaxation));
        tagsRepository.save(new Tags("TAG016", Tags.Tag.Meditation));
        tagsRepository.save(new Tags("TAG017", Tags.Tag.Motivation));
        tagsRepository.save(new Tags("TAG018", Tags.Tag.Test_Anxiety));
        tagsRepository.save(new Tags("TAG019", Tags.Tag.Test_Depression));
        tagsRepository.save(new Tags("TAG020", Tags.Tag.Test_Stress));
        tagsRepository.save(new Tags("TAG021", Tags.Tag.Academic_Stress));
        tagsRepository.save(new Tags("TAG022", Tags.Tag.Work_Stress));
        tagsRepository.save(new Tags("TAG023", Tags.Tag.Financial_Stress));
        tagsRepository.save(new Tags("TAG024", Tags.Tag.Relationship_Stress));
        tagsRepository.save(new Tags("TAG025", Tags.Tag.Self_Improvement_Stress));
        tagsRepository.save(new Tags("TAG026", Tags.Tag.Academic_Relationship));
        tagsRepository.save(new Tags("TAG027", Tags.Tag.Work_Relationship));
        tagsRepository.save(new Tags("TAG028", Tags.Tag.Financial_Relationship));
        tagsRepository.save(new Tags("TAG029", Tags.Tag.Relationship));
        tagsRepository.save(new Tags("TAG030", Tags.Tag.Self_Improvement));
        tagsRepository.save(new Tags("TAG031", Tags.Tag.Performance));
        tagsRepository.save(new Tags("TAG032", Tags.Tag.Confidence));
        tagsRepository.save(new Tags("TAG033", Tags.Tag.Self_Esteem));
        tagsRepository.save(new Tags("TAG034", Tags.Tag.Self_Awareness));
        tagsRepository.save(new Tags("TAG035", Tags.Tag.Self_Discipline));
        tagsRepository.save(new Tags("TAG036", Tags.Tag.Self_Reflection));
        tagsRepository.save(new Tags("TAG037", Tags.Tag.Self_Management));
        tagsRepository.save(new Tags("TAG038", Tags.Tag.Resilience));
        tagsRepository.save(new Tags("TAG039", Tags.Tag.Coping_Skills));
        tagsRepository.save(new Tags("TAG040", Tags.Tag.Problem_Solving));
        tagsRepository.save(new Tags("TAG041", Tags.Tag.Decision_Making));
        tagsRepository.save(new Tags("TAG042", Tags.Tag.Time_Management));
        tagsRepository.save(new Tags("TAG043", Tags.Tag.Stress_Management));
        tagsRepository.save(new Tags("TAG044", Tags.Tag.Emotional_Intelligence));
        tagsRepository.save(new Tags("TAG045", Tags.Tag.Emotional_Regulation));
        tagsRepository.save(new Tags("TAG046", Tags.Tag.Emotional_Expression));
        tagsRepository.save(new Tags("TAG047", Tags.Tag.Productivity));
        tagsRepository.save(new Tags("TAG048", Tags.Tag.Boundaries));
        tagsRepository.save(new Tags("TAG049", Tags.Tag.Self_Control));
        tagsRepository.save(new Tags("TAG050", Tags.Tag.Wellness));
        tagsRepository.save(new Tags("TAG051", Tags.Tag.Health));
        tagsRepository.save(new Tags("TAG052", Tags.Tag.Grief));
        tagsRepository.save(new Tags("TAG053", Tags.Tag.Support));
        tagsRepository.save(new Tags("TAG054", Tags.Tag.Healing));
        tagsRepository.save(new Tags("TAG055", Tags.Tag.Body_Image));
        tagsRepository.save(new Tags("TAG056", Tags.Tag.Personal_Development));
        tagsRepository.save(new Tags("TAG057", Tags.Tag.Self_Acceptance));
        tagsRepository.save(new Tags("TAG058", Tags.Tag.Self_Health));
        tagsRepository.save(new Tags("TAG059", Tags.Tag.Social_Support));
        tagsRepository.save(new Tags("TAG060", Tags.Tag.Social_Connectivity));
        tagsRepository.save(new Tags("TAG061", Tags.Tag.Social_Interaction));
        tagsRepository.save(new Tags("TAG062", Tags.Tag.Community_Engagement));
        tagsRepository.save(new Tags("TAG063", Tags.Tag.Community_Involvement));
        tagsRepository.save(new Tags("TAG064", Tags.Tag.Relationship_Building));
        tagsRepository.save(new Tags("TAG065", Tags.Tag.Relationship_Health));

        // Initialize Programs
        programRepository.save(new Programs("PRG001", "Stress Management",
                "Program to help manage stress", 20, 4, Programs.Status.Active,
                departmentRepository.findById("DP06").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG001", "TAG002", "TAG003"))),
                LocalDate.parse("2025-02-13"),"https://example.com/meeting1", Programs.Type.Online));
        programRepository.save(new Programs("PRG002", "Anxiety Support Group",
                "Support group for individuals with anxiety", 15, 6, Programs.Status.Active,
                departmentRepository.findById("DP06").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG004", "TAG005", "TAG006"))),
                LocalDate.parse("2025-02-15"),"https://example.com/meeting2", Programs.Type.Offline));
        programRepository.save(new Programs("PRG003", "Mindfulness Workshop",
                "Workshop on mindfulness techniques", 25, 3, Programs.Status.Active,
                departmentRepository.findById("DP03").orElseThrow(),
                psychologistRepository.findById("PSY001").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG007", "TAG008", "TAG009"))),
                LocalDate.parse("2025-02-18"),"https://example.com/meeting3", Programs.Type.Online));
        programRepository.save(new Programs("PRG004", "Depression Counseling",
                "Counseling for individuals with depression", 30, 2, Programs.Status.Active,
                departmentRepository.findById("DP07").orElseThrow(),
                psychologistRepository.findById("PSY002").orElseThrow(),
                new HashSet<Tags>(tagsRepository.findAllById(List.of("TAG010", "TAG011", "TAG012"))),
                LocalDate.parse("2025-02-28"),"https://example.com/meeting4", Programs.Type.Online));

        // Initialize Program Schedule        departmentRepository.save(new Department("DP01", "Child & Adolescent Psychology"));
        programScheduleRepository.save(new ProgramSchedule("SCH001", "PRG001", "Monday", LocalTime.parse("10:00:00"), LocalTime.parse("11:30:00")));
        programScheduleRepository.save(new ProgramSchedule("SCH002", "PRG002", "Tuesday", LocalTime.parse("14:00:00"), LocalTime.parse("15:30:00")));
        programScheduleRepository.save(new ProgramSchedule("SCH003", "PRG003", "Wednesday", LocalTime.parse("09:00:00"), LocalTime.parse("10:30:00")));

        // Initialize Program Participation
        programParticipationRepository.save(new ProgramParticipation("PP001", "S001", "PRG001", ProgramParticipation.Status.Completed, LocalDate.parse("2023-06-01"), LocalDate.parse("2023-06-30")));
        programParticipationRepository.save(new ProgramParticipation("PP002", "S002", "PRG002", ProgramParticipation.Status.Joined, LocalDate.parse("2023-07-01"), LocalDate.parse("2023-08-15")));

        // Initialize Categories
        categoryRepository.save(new Categories("CAT001", Categories.MentalHealthCategory.Anxiety));
        categoryRepository.save(new Categories("CAT002", Categories.MentalHealthCategory.Stress));
        categoryRepository.save(new Categories("CAT003", Categories.MentalHealthCategory.Depression));

        // Initialize Surveys
        surveyRepository.save(new Surveys("SUR001", "Stress Survey", "Survey to assess stress levels", "CAT001", userRepository.findByUsername("student_user").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR002", "Anxiety Assessment", "Assessment of anxiety symptoms", "CAT002", userRepository.findByUsername("student_user2").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR003", "Depression Screening", "Screening for depression", "CAT003", userRepository.findByUsername("student_user3").getUserId(), Surveys.Status.Finished));
        surveyRepository.save(new Surveys("SUR004", "Mood Assessment", "Assessment of mood", "CAT001", userRepository.findByUsername("student_user").getUserId(), Surveys.Status.Unfinished));

        // Initialize Survey Questions
        surveyQuestionRepository.save(new SurveyQuestions("Q001", "SUR001", "How often do you feel stressed?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q002", "SUR001", "How much does stress interfere with your daily life?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q003", "SUR002", "Do you experience excessive worry or fear?", "CAT002"));
        surveyQuestionRepository.save(new SurveyQuestions("Q004", "SUR002", "How often do you have panic attacks?", "CAT002"));
        surveyQuestionRepository.save(new SurveyQuestions("Q005", "SUR003", "Do you feel sad or hopeless most of the time?", "CAT003"));
        surveyQuestionRepository.save(new SurveyQuestions("Q006", "SUR003", "Have you lost interest in activities you once enjoyed?", "CAT003"));
        surveyQuestionRepository.save(new SurveyQuestions("Q007", "SUR004", "How are you feeling today?", "CAT001"));
        surveyQuestionRepository.save(new SurveyQuestions("Q008", "SUR004", "Are you having a good day?", "CAT001"));

        // Initialize Answerss
        answersRepository.save(new Answers("A001", "Q001", "Never", 0));
        answersRepository.save(new Answers("A002", "Q001", "Sometimes", 1));
        answersRepository.save(new Answers("A003", "Q001", "Often", 2));
        answersRepository.save(new Answers("A004", "Q001", "Always", 3));
        answersRepository.save(new Answers("A005", "Q002", "Never", 0));
        answersRepository.save(new Answers("A006", "Q002", "Sometimes", 1));
        answersRepository.save(new Answers("A007", "Q002", "Moderately", 2));
        answersRepository.save(new Answers("A008", "Q002", "Very much", 3));
        answersRepository.save(new Answers("A009", "Q003", "Rarely", 0));
        answersRepository.save(new Answers("A010", "Q003", "Sometimes", 1));
        answersRepository.save(new Answers("A011", "Q003", "Often", 2));
        answersRepository.save(new Answers("A012", "Q003", "Always", 3));
        answersRepository.save(new Answers("A013", "Q004", "Never", 0));
        answersRepository.save(new Answers("A014", "Q004", "Once a month", 1));
        answersRepository.save(new Answers("A015", "Q004", "Once a week", 2));
        answersRepository.save(new Answers("A016", "Q004", "Multiple times a week", 3));
        answersRepository.save(new Answers("A017", "Q005", "Never", 0));
        answersRepository.save(new Answers("A018", "Q005", "Sometimes", 1));
        answersRepository.save(new Answers("A019", "Q005", "Often", 2));
        answersRepository.save(new Answers("A020", "Q005", "Always", 3));
        answersRepository.save(new Answers("A021", "Q006", "Never", 0));
        answersRepository.save(new Answers("A022", "Q006", "Sometimes", 1));
        answersRepository.save(new Answers("A023", "Q006", "Often", 2));
        answersRepository.save(new Answers("A024", "Q006", "Always", 3));
        answersRepository.save(new Answers("A025", "Q007", "Good", 0));
        answersRepository.save(new Answers("A026", "Q007", "Fair", 1));
        answersRepository.save(new Answers("A027", "Q007", "Bad", 2));
        answersRepository.save(new Answers("A028", "Q007", "Very bad", 3));
        answersRepository.save(new Answers("A029", "Q008", "Yes", 0));
        answersRepository.save(new Answers("A030", "Q008", "No", 1));
        answersRepository.save(new Answers("A031", "Q008", "Not sure", 2));
        answersRepository.save(new Answers("A032", "Q008", "Not at all", 3));

        // Initialize Survey Results
        surveyResultRepository.save(new SurveyResults("R001", "S001", "Q001", "A002"));
        surveyResultRepository.save(new SurveyResults("R002", "S001", "Q002", "A006"));
        surveyResultRepository.save(new SurveyResults("R003", "S001", "Q003", "A010"));
        surveyResultRepository.save(new SurveyResults("R004", "S001", "Q004", "A014"));
        surveyResultRepository.save(new SurveyResults("R005", "S001", "Q005", "A018"));
        surveyResultRepository.save(new SurveyResults("R006", "S001", "Q006", "A022"));

        surveyResultRepository.save(new SurveyResults("R007", "S002", "Q001", "A001"));
        surveyResultRepository.save(new SurveyResults("R008", "S002", "Q002", "A005"));
        surveyResultRepository.save(new SurveyResults("R009", "S002", "Q003", "A009"));
        surveyResultRepository.save(new SurveyResults("R010", "S002", "Q004", "A013"));
        surveyResultRepository.save(new SurveyResults("R011", "S002", "Q005", "A017"));
        surveyResultRepository.save(new SurveyResults("R012", "S002", "Q006", "A021"));

        surveyResultRepository.save(new SurveyResults("R013", "S003", "Q001", "A003"));
        surveyResultRepository.save(new SurveyResults("R014", "S003", "Q002", "A007"));
        surveyResultRepository.save(new SurveyResults("R015", "S003", "Q003", "A011"));
        surveyResultRepository.save(new SurveyResults("R016", "S003", "Q004", "A015"));
        surveyResultRepository.save(new SurveyResults("R017", "S003", "Q005", "A019"));
        surveyResultRepository.save(new SurveyResults("R018", "S003", "Q006", "A023"));

        // Initialize User Logs
        userLogRepository.save(new UserLogs("L001", userRepository.findByUsername("student_user").getUserId(), "192.168.0.1"));
        userLogRepository.save(new UserLogs("L002", userRepository.findByUsername("student_user2").getUserId(), "192.168.0.2"));

        // Initialize Blogs
        blogRepository.save(new Article("B001", "Managing Stress", userRepository.findByUsername("psychologist_user").getUserId(), "Tips for managing stress..."));
        blogRepository.save(new Article("B002", "Overcoming Anxiety", userRepository.findByUsername("psychologist_user2").getUserId(), "Strategies to cope with anxiety..."));

        // Initialize Appointments
        appointmentRepository.save(new Appointments("APP001", "TSPSY00120022501", "S001", "PSY001", StatusEnum.Scheduled));
        appointmentRepository.save(new Appointments("APP002","TSPSY00221022501", "S002", "PSY002", StatusEnum.Scheduled));

        // Initialize Notifications
        notificationRepository.save(new Notifications("NOT001", userRepository.findByUsername("psychologist_user").getUserId(), "Appointment Scheduled", "Your appointment is scheduled for 2023-06-15 at 10:00 AM", Notifications.Type.Appointment));
        notificationRepository.save(new Notifications("NOT002", userRepository.findByUsername("student_user").getUserId(), "Survey Available", "A new survey is available for you to complete", Notifications.Type.Survey));
    }

    @Override
    public void run(String... args) {
        try {
            if (userRepository.count() != 0) {
                return;
            }
            this.initialize();
        } catch (Exception e) {
            e.initCause(e);
        }
    }
}