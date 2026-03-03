package com.educonnect.controller;


import com.educonnect.model.audit.AuditLog;
import com.educonnect.service.contract.audit.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditLogService auditLogService;
    @GetMapping("{userId}")
    public ResponseEntity<List<AuditLog>> viewAudits(@PathVariable UUID userId) throws Exception {
        return ResponseEntity.ok(auditLogService.findAuditLogByUserId(userId));
    }
}
