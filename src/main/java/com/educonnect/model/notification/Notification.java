package com.educonnect.model.notification;

import com.educonnect.model.user.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID notificationId;

    private UUID entityId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType category;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
