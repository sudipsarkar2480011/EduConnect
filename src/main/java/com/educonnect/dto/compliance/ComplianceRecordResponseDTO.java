package com.educonnect.dto.compliance;

import com.educonnect.model.compliance.ComplianceType;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ComplianceRecordResponseDTO {
    private UUID complianceRecordID;
    private UUID userId;
    private ComplianceType type;
    private String result;
    private LocalDate date;
    private List<String> notes;
}