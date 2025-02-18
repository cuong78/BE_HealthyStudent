package com.healthy.backend.repository;

import com.healthy.backend.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Students, String> {

    Students findByUserID(String userID);

    Students findByStudentID(String studentID);
} 