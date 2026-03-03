package com.educonnect.repo.audit;

import com.educonnect.model.audit.Audit;
import com.educonnect.model.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepo extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserUserId(UUID userId);
}
