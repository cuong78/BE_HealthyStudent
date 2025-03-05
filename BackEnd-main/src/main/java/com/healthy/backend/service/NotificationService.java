package com.healthy.backend.service;

import com.healthy.backend.dto.notification.NotificationResponse;
import com.healthy.backend.entity.Notifications;
import com.healthy.backend.enums.NotificationType;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.NotificationMapper;
import com.healthy.backend.repository.NotificationRepository;
import com.healthy.backend.repository.UserRepository;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final TokenService tokenService;
    private final GeneralService __;



    public void createAppointmentNotification(String userId, String title, String message, String appointmentId) {
        createNotification(userId, title, message, appointmentId, NotificationType.APPOINTMENT);
    }

    public void createOnLeaveNotification(String userId, String title, String message, String leaveRequestId) {
        createNotification(userId, title, message, leaveRequestId, NotificationType.ON_LEAVE);
    }

    public void createProgramNotification(String userId, String title, String message, String programId) {
        createNotification(userId, title, message, programId, NotificationType.PROGRAM);
    }

    public void createSurveyNotification(String userId, String title, String message, String surveyId) {
        createNotification(userId, title, message, surveyId, NotificationType.SURVEY);
    }

    public List<NotificationResponse> getUserReadNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        checkUserPermission(request, finalUserId);
        return notificationMapper.buildNotificationResponseList(
                notificationRepository.findByUserIDAndIsReadTrueOrderByCreatedAtDesc(finalUserId));
    }

    public List<NotificationResponse> getUserUnreadNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        checkUserPermission(request, finalUserId);
        return notificationMapper.buildNotificationResponseList(
                notificationRepository.findByUserIDAndIsReadFalseOrderByCreatedAtDesc(finalUserId));
    }

    public List<NotificationResponse> getUserNotifications(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        checkUserPermission(request, finalUserId);
        return notificationMapper.buildNotificationResponseList(
                notificationRepository.findByUserIDOrderByCreatedAtDesc(finalUserId));
    }

    public void markAsRead(String notificationId, HttpServletRequest request) {
        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getAllNotifications(HttpServletRequest request) {
        if (tokenService.isManager(request)) {
            throw new ResourceInvalidException("You can not get database notifications");
        }
        return notificationMapper.buildNotificationResponseList(
                notificationRepository.findAll());
    }

    private void createNotification(String userId, String title, String message, String entityId, NotificationType notificationType) {
        Notifications notification = new Notifications();
        notification.setNotificationID(__.generateNextNotificationID());
        notification.setUserID(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(notificationType);

        switch (notificationType) {
            case APPOINTMENT:
                notification.setAppointmentID(entityId);
                break;
            case PROGRAM:
                notification.setProgramID(entityId);
                break;
            case SURVEY:
                notification.setSurveyID(entityId);
                break;
            case DONE:
                // Logic for DONE
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }

        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    private String validateUserID(HttpServletRequest request, String userId) {
        if(userId == null) {
            return tokenService.retrieveUser(request).getUserId();
        }
        if(!userRepository.existsById(userId)) {
            return tokenService.retrieveUser(request).getUserId();
        }
        return userId;
    }
    private void checkUserPermission(HttpServletRequest request, String finalUserId) {
        if (tokenService.validateUID(request, finalUserId) && !tokenService.isManager(request)) {
            throw new ResourceInvalidException("You cannot get other users' notifications");
        }
    }
}