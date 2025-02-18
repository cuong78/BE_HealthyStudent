package com.healthy.backend.repository;

import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlots, String> {
    @Query("SELECT ts FROM TimeSlots ts JOIN FETCH ts.psychologist WHERE ts.timeSlotsID = :id")
    Optional<TimeSlots> findByIdWithPsychologist(@Param("id") String id);

    List<TimeSlots> findBySlotDateAndPsychologist(LocalDate slotDate, Psychologists psychologist);

    List<TimeSlots> findByPsychologist(Psychologists psychologist);
}
