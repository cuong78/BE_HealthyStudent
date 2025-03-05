package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import com.healthy.backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, String> , JpaSpecificationExecutor<Appointments> {

    List<Appointments> findByStudentID(String studentID);
    List<Appointments> findByPsychologistID(String psychologistID);
    @Query("SELECT a.appointmentID FROM Appointments a ORDER BY a.appointmentID DESC LIMIT 1")
    String findLastAppointmentId();

    List<Appointments> findByPsychologistIDAndStatusAndFeedbacksNotNull(String psychologistId, AppointmentStatus status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointments a " +
            "WHERE a.studentID = :studentId AND a.timeSlotsID = :timeSlotId")
    boolean existsByStudentIDAndTimeSlotsID(
            @Param("studentId") String studentId,
            @Param("timeSlotId") String timeSlotId);



    @Query("SELECT a FROM Appointments a " +
            "JOIN FETCH a.timeSlot " +
            "JOIN FETCH a.student " +
            "JOIN FETCH a.psychologist " +
            "WHERE a.psychologistID = :psychologistId")
    List<Appointments> findByPsychologistIDWithDetails(String psychologistId);

    long countByPsychologistID(String psychologistID);
} 