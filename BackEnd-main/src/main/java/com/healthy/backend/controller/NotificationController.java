package com.healthy.backend.controller;

import com.healthy.backend.dto.notification.NotificationResponse;
import com.healthy.backend.entity.Notifications;
import com.healthy.backend.entity.Users;
import com.healthy.backend.mapper.NotificationMapper;
import com.healthy.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Notification Controller", description = "Quản lý thông báo")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Operation(summary = "Lấy tất cả thông báo của người dùng")
    @GetMapping("/{id}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable String id) {
        List<Notifications> notifications = notificationService.getUserNotifications(id);
        List<NotificationResponse> responses = notifications.stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Đánh dấu thông báo đã đọc")
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);


        return ResponseEntity.ok().build();
    }


}