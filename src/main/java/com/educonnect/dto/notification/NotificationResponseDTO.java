package com.educonnect.dto.notification;

import com.educonnect.model.notification.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class NotificationResponseDTO {
    private UUID id;
    private String message;
    private NotificationType category;
    private Boolean status;
    private LocalDateTime createdDate;
    private UUID courseId;
}
