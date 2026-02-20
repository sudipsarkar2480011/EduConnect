package com.educonnect.model.notification;

import com.educonnect.model.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    private Integer entityId;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType category;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
