package com.educonnect.repo.compliance;

import com.educonnect.model.compliance.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComplianceRecordRepo extends JpaRepository<ComplianceRecord, UUID> {

}
