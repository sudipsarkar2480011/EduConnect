package com.educonnect.controller;

import com.educonnect.dto.notification.NotificationResponseDTO;
import com.educonnect.service.contract.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/av1/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAll(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @PutMapping("/mark-as-seen/{userId}")
    public ResponseEntity<String> markAsSeen(@PathVariable UUID userId) {
        notificationService.markAllAsSeen(userId);
        return ResponseEntity.ok("Notifications updated to seen.");
    }
}
