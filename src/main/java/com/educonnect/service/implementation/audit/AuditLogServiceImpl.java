package com.educonnect.service.implementation.audit;

import com.educonnect.config.UserRepo;
import com.educonnect.model.audit.Action;
import com.educonnect.model.audit.AuditLog;
import com.educonnect.model.user.User;
import com.educonnect.repo.audit.AuditLogRepo;
import com.educonnect.service.contract.audit.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final UserRepo userRepo;
    private final AuditLogRepo auditLogRepo;

    @Override
    public AuditLog createAudit(UUID userId, Action action, String resource) throws Exception {
        User u = userRepo.findById(userId)
                .orElseThrow(()->new Exception("User not found"));
        AuditLog log = AuditLog.builder().action(action)
                .resource(resource).user(u)
                .build();
        u.getAuditLogs().add(log);
        userRepo.save(u);
        return  log;
    }

    @Override
    public List<AuditLog> findAuditLogByUserId(UUID userID) throws Exception {
        return auditLogRepo.findByUserUserId(userID);
    }
}
