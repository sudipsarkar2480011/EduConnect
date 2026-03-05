package com.educonnect.service.contract.audit;


import com.educonnect.model.audit.Action;
import com.educonnect.model.audit.AuditLog;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {
    AuditLog createAudit(UUID userId, Action action, String resource) throws Exception;
    List<AuditLog> findAuditLogByUserId(UUID userID) throws Exception;
    List<AuditLog> findAuditLogByUserId(UUID userID, Integer from, Integer pageSize) throws Exception;
    List<AuditLog> findAuditByResource(String resource, Integer from, Integer pageSize) throws  Exception;
}
