package com.healthy.backend.repository;

import com.healthy.backend.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, String> {
    NotificationSchedule findFirstByOrderByNotificationTimeAsc();
}
