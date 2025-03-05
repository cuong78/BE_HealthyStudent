package com.healthy.backend.repository;

import com.healthy.backend.entity.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogs, String> {

    @Query("SELECT ul.logID FROM UserLogs ul ORDER BY ul.logID DESC LIMIT 1")
    String findLastUserLogId();
} 