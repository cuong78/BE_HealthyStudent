package com.healthy.backend.service;

import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.TimeSlots;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;

    public void createNotification(String userId, String title, String message, Notifications.Type type) {
        Notifications notification = new Notifications();
        notification.setNotificationID(UUID.randomUUID().toString());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);
    }

    public List<Notifications> getUserNotifications(String userId) {

        return notificationRepository.findByUserIDOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(String notificationId) {
        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}