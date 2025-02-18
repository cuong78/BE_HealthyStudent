package com.healthy.backend.repository;

import com.healthy.backend.entity.Psychologists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologists, String> {

    Psychologists findByUserID(String userID);
    List<Psychologists> findByDepartmentDepartmentID(String departmentID);
} 