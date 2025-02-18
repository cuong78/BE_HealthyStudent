package com.healthy.backend.repository;

import com.healthy.backend.entity.ProgramSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProgramScheduleRepository extends JpaRepository<ProgramSchedule, String> {


    @Modifying
    @Transactional
    @Query("DELETE FROM ProgramSchedule p WHERE p.programID = :programId")
    void deleteByProgramId(@Param("programId") String programId);
} 