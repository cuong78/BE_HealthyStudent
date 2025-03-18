package com.healthy.backend.dto.manager;


import com.healthy.backend.stats.AppointmentStats;
import com.healthy.backend.stats.DepartmentStats;
import com.healthy.backend.stats.SurveyStats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDashboardResponse {
    private SurveyStats surveyStats;
    private AppointmentStats appointmentStats;
    private DepartmentStats departmentStats;
    // getters & setters
}
