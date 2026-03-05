package com.educonnect.repo.audit;


import com.educonnect.model.audit.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepo extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserUserId(UUID userId);
    Page<AuditLog> findAllByUserUserId(UUID userId, Pageable pageable);
    Page<AuditLog> findAllByResource(String resource, Pageable pageable);
}
