package com.healthy.backend.repository;

import com.healthy.backend.entity.PsychologistKPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsychologistKPIRepository extends JpaRepository<PsychologistKPI, String> {
    PsychologistKPI findByPsychologistIdAndMonthAndYear(String psychologistId, int month, int year);

}