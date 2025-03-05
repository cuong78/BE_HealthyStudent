package com.healthy.backend.repository;

import com.healthy.backend.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, String> {
    List<Notifications> findByUserIDOrderByCreatedAtDesc(String userId);

    @Query("SELECT n.notificationID  FROM Notifications n ORDER BY n.notificationID DESC LIMIT 1")
    String findLastNotificationId();
    
    @Query("SELECT n.notificationID FROM Notifications n ORDER BY n.createdAt DESC")
    List<String> findLastNotificationID();

    List<Notifications> findByUserIDAndIsReadTrueOrderByCreatedAtDesc(String userID);
    List<Notifications> findByUserIDAndIsReadFalseOrderByCreatedAtDesc(String userID);
}