package com.educonnect.model.audit;

//AuditLog(AuditID, UserID, Action, Resource, Timestamp)

import com.educonnect.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class AuditLog {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID auditLogId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Action action;

    private String resource; //entity name and entity id , entity type

    @CurrentTimestamp
    private LocalDateTime timestamp ;
}
