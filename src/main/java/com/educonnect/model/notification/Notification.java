package com.educonnect.model.notification;

import com.educonnect.model.user.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID notificationUuid = UUID.randomUUID();

    private Integer entityId;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType category;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
