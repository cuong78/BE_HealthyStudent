package com.healthy.backend.repository;

import com.healthy.backend.entity.Parents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parents, String> {
    Parents findByUserID(String userID);

    @Query("SELECT p FROM Parents p JOIN FETCH p.students WHERE p.user.userId = :userId")
    Parents findByUserIDWithStudents(@Param("userId") String userId);

    @Query("SELECT p.parentID FROM Parents p ORDER BY p.parentID DESC LIMIT 1")
    String findLastParentId();

} 