package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import com.healthy.backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, String> , JpaSpecificationExecutor<Appointments> {

    List<Appointments> findByStudentID(String studentID);
    List<Appointments> findByPsychologistID(String psychologistID);
    @Query("SELECT a.appointmentID FROM Appointments a ORDER BY a.appointmentID DESC LIMIT 1")
    String findLastAppointmentId();


    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointments a " +
            "WHERE a.studentID = :studentId " +
            "AND a.timeSlotsID = :timeSlotId " +
            "AND a.status != com.healthy.backend.enums.AppointmentStatus.CANCELLED")
    boolean existsByStudentIDAndTimeSlotsID(
            @Param("studentId") String studentId,
            @Param("timeSlotId") String timeSlotId);





    @Query("SELECT COUNT(a) FROM Appointments a " +
            "WHERE (:startDate IS NULL OR a.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR a.createdAt <= :endDate)")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate,
                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) FROM Appointments a " +
            "WHERE a.status = :status " +
            "AND (:startDate IS NULL OR a.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR a.createdAt <= :endDate)")
    Long countByStatusAndDateRange(@Param("status") AppointmentStatus status,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointments a " +
            "WHERE a.status = :status " +
            "AND (:start IS NULL OR a.createdAt >= :start) " +
            "AND (:end IS NULL OR a.createdAt <= :end)")
    List<Appointments> findByStatusAndDateRange(
            @Param("status") AppointmentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
} 