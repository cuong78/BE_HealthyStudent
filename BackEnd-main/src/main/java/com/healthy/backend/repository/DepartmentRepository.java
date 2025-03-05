package com.healthy.backend.repository;

import com.healthy.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    @Query("SELECT d.departmentID FROM Department d ORDER BY d.departmentID DESC LIMIT 1")
    String findLastDepartmentId();
}