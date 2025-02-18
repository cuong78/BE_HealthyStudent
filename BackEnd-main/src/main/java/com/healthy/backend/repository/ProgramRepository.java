package com.healthy.backend.repository;

import com.healthy.backend.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Programs, String> {
    List<Programs> findByFacilitatorID(String userId);

    @Query("SELECT p.programID FROM Programs p ORDER BY p.programID DESC LIMIT 1")
    String findLastProgramId();
}