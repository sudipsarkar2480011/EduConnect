package com.educonnect.service.contract.compliance;

import com.educonnect.dto.compliance.ComplianceRecordRequestDTO;
import com.educonnect.dto.compliance.ComplianceRecordResponseDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ComplianceRecordService {
    ComplianceRecordResponseDTO createRecord(ComplianceRecordRequestDTO requestDTO) throws UserNotFoundException;
    ComplianceRecordResponseDTO getRecordById(UUID id);
    List<ComplianceRecordResponseDTO> getAllRecords();
    ComplianceRecordResponseDTO updateRecord(UUID id, ComplianceRecordRequestDTO dto);
}
