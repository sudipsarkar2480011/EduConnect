package com.educonnect.dto.compliance;

import com.educonnect.model.compliance.ComplianceType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ComplianceRecordRequestDTO {
    private UUID userId; // Works for both Student and Teacher IDs
    private ComplianceType type;
    private String result;
    private LocalDate date;
    private List<String> notes;
}