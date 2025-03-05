package com.healthy.backend.repository;

import com.healthy.backend.entity.Psychologists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologists, String> {
    Psychologists findByUserID(String userID);
    List<Psychologists> findByDepartmentDepartmentID(String departmentID);

    @Query ("SELECT p.psychologistID FROM Psychologists p ORDER BY p.psychologistID DESC LIMIT 1")
    String findLastPsychologistId();

    Psychologists findByPsychologistID(String psychologistId);

} 