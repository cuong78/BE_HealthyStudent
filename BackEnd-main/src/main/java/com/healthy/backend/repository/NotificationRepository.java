package com.healthy.backend.repository;

import com.healthy.backend.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notifications, String> {
    List<Notifications> findByUserIDOrderByCreatedAtDesc(String userId);
}