package com.healthy.backend.service;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.ManagerDashboardResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.repository.*;
import com.healthy.backend.stats.AppointmentStats;
import com.healthy.backend.stats.DepartmentStats;
import com.healthy.backend.stats.SurveyStats;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final PsychologistKPIRepository kpiRepository;
    private final NotificationScheduleRepository notificationScheduleRepository;
    private  final GeneralService generalService;

    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;

    private NotificationSchedule cachedSchedule = null;
    private LocalDateTime lastScheduleCheck = null;
    private LocalDate lastNotificationDate = null;
    private final Object notificationLock = new Object();
    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);





    public ManagerDashboardResponse getDashboardStats(String filter, Integer value) {
        LocalDate[] dateRange = resolveDateRange(filter, value);

        return new ManagerDashboardResponse(
                calculateSurveyStats(dateRange),
                calculateAppointmentStats(dateRange),
                calculateDepartmentStats(dateRange)

        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDefaultKPIs() {
        int currentYear = Year.now().getValue();
        List<Psychologists> psychologists = psychologistRepository.findAll();

        psychologists.forEach(psychologist -> {
            for (int month = 1; month <= 12; month++) {
                PsychologistKPI existingKpi = kpiRepository.findByPsychologistIdAndMonthAndYear(
                        psychologist.getPsychologistID(),
                        month,
                        currentYear
                );

                if (existingKpi == null) {
                    PsychologistKPI kpi = new PsychologistKPI();
                    kpi.setId(psychologist.getPsychologistID() + "-" + month + "-" + currentYear);
                    kpi.setPsychologistId(psychologist.getPsychologistID());
                    kpi.setMonth(month);
                    kpi.setYear(currentYear);
                    kpi.setTargetSlots(30);
                    kpi.setAchievedSlots(0);
                    kpiRepository.save(kpi);
                }
            }
        });
    }


    private SurveyStats calculateSurveyStats(LocalDate[] dateRange) {
        Map<String, Double> rates = new HashMap<>();

        // Convert LocalDate to LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        // Count survey results by category within date range
        Map<String, Long> resultCountsByCategory = new HashMap<>();
        long totalResults = 0;

        // Get all surveys with their categories
        List<Surveys> allSurveys = surveyRepository.findAll();
        Map<String, SurveyCategory> surveyCategories = allSurveys.stream()
                .collect(Collectors.toMap(Surveys::getSurveyID, survey -> survey.getCategory().getCategoryName()));

        // Initialize counters for each category
        for (SurveyCategory category : SurveyCategory.values()) {
            resultCountsByCategory.put(category.name(), 0L);
        }

        // Get all survey results within the date range
        List<SurveyResult> allResults;
        if (startDateTime != null && endDateTime != null) {
            // This would require a custom query in the repository
            // For now, we'll filter after fetching all results
            allResults = surveyResultRepository.findAll().stream()
                    .filter(result -> !result.getCreatedAt().isBefore(startDateTime) &&
                            !result.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
        } else {
            allResults = surveyResultRepository.findAll();
        }

        // Count results by category
        for (SurveyResult result : allResults) {
            String surveyId = result.getSurveyID();
            SurveyCategory category = surveyCategories.get(surveyId);

            if (category != null) {
                String categoryName = category.name();
                resultCountsByCategory.put(categoryName, resultCountsByCategory.get(categoryName) + 1);
                totalResults++;
            }
        }

        // Calculate percentages
        for (SurveyCategory category : SurveyCategory.values()) {
            String categoryName = category.name();
            long count = resultCountsByCategory.get(categoryName);
            double percentage = totalResults > 0 ? (count * 100.0) / totalResults : 0;
            rates.put(categoryName, percentage);
        }

        SurveyStats stats = new SurveyStats();
        stats.setSurveyParticipationRates(rates);
        return stats;
    }





    private final  NotificationService notificationService;

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi phút
    public void sendWeeklyKpiReminders() {
        synchronized (notificationLock) {
            try {
                // 1. Cập nhật cache lịch trình
                refreshScheduleCache();

                // 2. Kiểm tra điều kiện gửi
                if (cachedSchedule == null) return;

                LocalDate today = LocalDate.now();
                LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

                // 3. Kiểm tra ngày và giờ
                if (shouldSendNotification(today, now)) {
                    // 4. Thực hiện gửi thông báo
                    sendNotifications();

                    // 5. Cập nhật trạng thái
                    lastNotificationDate = today;
                }
            } catch (Exception e) {
                // Xử lý exception
                logger.error("Error sending KPI reminders: {}", e.getMessage(), e);
            }
        }
    }

    private void refreshScheduleCache() {
        if (cachedSchedule == null
                || lastScheduleCheck == null
                || lastScheduleCheck.isBefore(LocalDateTime.now().minusMinutes(5))) {
            cachedSchedule = notificationScheduleRepository.findFirstByOrderByNotificationTimeAsc();
            lastScheduleCheck = LocalDateTime.now();
        }
    }
    private boolean shouldSendNotification(LocalDate today, LocalTime now) {
        return cachedSchedule.getNotificationDay() == today.getDayOfWeek()
                && now.equals(cachedSchedule.getNotificationTime().truncatedTo(ChronoUnit.MINUTES))
                && (lastNotificationDate == null || !lastNotificationDate.equals(today));
    }
    private void sendNotifications() {
        LocalDate todayDate = LocalDate.now();
        int currentMonth = todayDate.getMonthValue();
        int currentYear = todayDate.getYear();

        List<Psychologists> psychologists = psychologistRepository.findAll();

        for (Psychologists psychologist : psychologists) {
            PsychologistKPI kpi = kpiRepository.findByPsychologistIdAndMonthAndYear(
                    psychologist.getPsychologistID(),
                    currentMonth,
                    currentYear
            );

            if (kpi != null && kpi.getTargetSlots() > kpi.getAchievedSlots()) {
                int remaining = kpi.getTargetSlots() - kpi.getAchievedSlots();
                String message = String.format(
                        "You need to reach %d more slots in month %d to complete KPI target.",
                        remaining,
                        currentMonth
                );

                notificationService.createAppointmentNotification(
                        psychologist.getUserID(),
                        "Weekly KPI reminder",
                        message,
                        null
                );
            }
        }
    }

    @Transactional
    public void setKpiForPsychologist(String psychologistId, int month, int year, int targetSlots) {
        PsychologistKPI kpi = kpiRepository.findByPsychologistIdAndMonthAndYear(psychologistId, month, year);

        if (kpi == null) {
            kpi = new PsychologistKPI();
            kpi.setId(psychologistId + "-" + month + "-" + year);
            kpi.setPsychologistId(psychologistId);
            kpi.setMonth(month);
            kpi.setYear(year);
        }

        kpi.setTargetSlots(targetSlots);
        kpiRepository.save(kpi);
    }

    @Transactional
    public void setNotificationSchedule(LocalTime notificationTime, DayOfWeek notificationDay) {
        NotificationSchedule schedule = notificationScheduleRepository.findFirstByOrderByNotificationTimeAsc();

        if (schedule == null) {
            schedule = new NotificationSchedule();
            schedule.setId("notification-schedule-1");
        }

        schedule.setNotificationTime(notificationTime);
        schedule.setNotificationDay(notificationDay);
        notificationScheduleRepository.save(schedule);

        cachedSchedule = schedule;
        lastScheduleCheck = LocalDateTime.now();
    }


    // Method to get appointment statistics
    public AppointmentStatsResponse getAppointmentStats() {
        List<Appointments> appointments = appointmentRepository.findAll();
        Map<String, Long> appointmentCounts = appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getStatus().name(), Collectors.counting()));

        AppointmentStatsResponse response = new AppointmentStatsResponse();
        response.setAppointmentCounts(appointmentCounts);
        return response;
    }

    public List<PsychologistStatsResponse> getPsychologistStats() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        List<Appointments> allAppointments = appointmentRepository.findAll();

        List<PsychologistStatsResponse> responses = new ArrayList<>();
        for (Psychologists psychologist : psychologists) {
            // Filter appointments belonging to the current psychologist
            List<Appointments> filteredAppointments = allAppointments.stream()
                    .filter(a -> psychologist.getPsychologistID().equals(a.getPsychologistID()))
                    .collect(Collectors.toList());

            long appointmentCount = filteredAppointments.size();
            double averageRating = filteredAppointments.stream()
                    .filter(a -> a.getRating() != null)
                    .mapToInt(Appointments::getRating)
                    .average()
                    .orElse(0.0);

            PsychologistStatsResponse statResponse = new PsychologistStatsResponse();
            statResponse.setPsychologistId(psychologist.getPsychologistID());
            statResponse.setFullName(psychologist.getFullNameFromUser());
            statResponse.setAppointmentCount(appointmentCount);
            statResponse.setAverageRating(averageRating);
            responses.add(statResponse);
        }
        return responses;
    }



    private LocalDate[] resolveDateRange(String filter, Integer value) {
        LocalDate now = LocalDate.now();
        if (filter == null) return new LocalDate[]{null, null};

        return switch (filter.toLowerCase()) {
            case "week" -> {
                int week = value != null ? value : now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                LocalDate start = now.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week).with(DayOfWeek.MONDAY);
                yield new LocalDate[]{start, start.plusDays(6)};
            }
            case "month" -> {
                int month = value != null ? value : now.getMonthValue();
                YearMonth yearMonth = YearMonth.of(now.getYear(), month);
                yield new LocalDate[]{yearMonth.atDay(1), yearMonth.atEndOfMonth()};
            }
            case "year" -> {
                int year = value != null ? value : now.getYear();
                yield new LocalDate[]{LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)};
            }
            default -> new LocalDate[]{null, null};
        };
    }


    private AppointmentStats calculateAppointmentStats(LocalDate[] dateRange) {
        Map<String, Double> distribution = new HashMap<>();

        // Chuyển đổi LocalDate thành LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        long total = appointmentRepository.countByDateRange(startDateTime, endDateTime);
        if (total == 0) return new AppointmentStats();

        Arrays.stream(AppointmentStatus.values()).forEach(status -> {
            long count = appointmentRepository.countByStatusAndDateRange(status, startDateTime, endDateTime);
            double percentage = (count * 100.0) / total;
            distribution.put(status.name(), percentage);
        });

        AppointmentStats stats = new AppointmentStats();
        stats.setStatusDistribution(distribution);
        return stats;
    }
    private DepartmentStats calculateDepartmentStats(LocalDate[] dateRange) {
        Map<String, Double> distribution = new HashMap<>();

        // Chuyển đổi LocalDate sang LocalDateTime
        LocalDateTime startDateTime = dateRange[0] != null ? dateRange[0].atStartOfDay() : null;
        LocalDateTime endDateTime = dateRange[1] != null ? dateRange[1].atTime(23, 59, 59) : null;

        // Lấy tất cả appointments đã hoàn thành trong khoảng thời gian
        List<Appointments> completedAppointments = appointmentRepository
                .findByStatusAndDateRange(AppointmentStatus.COMPLETED, startDateTime, endDateTime);

        // Tổng số appointments đã hoàn thành
        long totalCompleted = completedAppointments.size();

        if (totalCompleted == 0) return new DepartmentStats(distribution);

        // Nhóm theo department và tính tỷ lệ
        completedAppointments.stream()
                .collect(Collectors.groupingBy(
                        appointment -> appointment.getPsychologist().getDepartment().getName(),
                        Collectors.counting()
                ))
                .forEach((departmentName, count) -> {
                    double percentage = (count * 100.0) / totalCompleted;
                    distribution.put(departmentName, percentage);
                });

        return new DepartmentStats(distribution);
    }


}