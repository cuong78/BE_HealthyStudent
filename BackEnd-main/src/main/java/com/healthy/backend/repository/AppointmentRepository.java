package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, String> {

    List<Appointments> findByStudentID(String studentID);
    List<Appointments> findByPsychologistID(String psychologistID);

    @Query("SELECT a.appointmentID FROM Appointments a ORDER BY a.appointmentID DESC LIMIT 1")
    String findLastAppointmentId();
} 