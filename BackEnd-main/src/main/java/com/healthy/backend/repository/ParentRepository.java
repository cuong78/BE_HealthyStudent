package com.healthy.backend.repository;

import com.healthy.backend.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);

    @Query("SELECT p FROM Parents p JOIN FETCH p.students WHERE p.user.userId = :userId")
    Parents findByUserIDWithStudents(@Param("userId") String userId);

} 