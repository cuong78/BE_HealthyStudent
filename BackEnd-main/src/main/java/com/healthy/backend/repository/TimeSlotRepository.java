package com.healthy.backend.repository;

import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlots, String> {
    @Query("SELECT ts FROM TimeSlots ts JOIN FETCH ts.psychologist WHERE ts.timeSlotsID = :id")
    Optional<TimeSlots> findByIdWithPsychologist(@Param("id") String id);



    @Query("SELECT ts FROM TimeSlots ts " +
            "WHERE ts.psychologist.psychologistID = :psychologistId " +
            "AND ts.slotDate = :date")
    List<TimeSlots> findByPsychologistIdAndDate(
            @Param("psychologistId") String psychologistId,
            @Param("date") LocalDate date);

    @Query("SELECT ts FROM TimeSlots ts " +
            "WHERE ts.psychologist.psychologistID = :psychologistId")
    List<TimeSlots> findByPsychologistId(@Param("psychologistId") String psychologistId);


    boolean existsByPsychologistAndSlotDateAndStartTimeAndEndTime(
            Psychologists psychologist,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    );


}
