package com.educonnect.service.implementation.compliance;

import com.educonnect.config.UserRepo;
import com.educonnect.dto.compliance.*;
import com.educonnect.exception.custom_exceptions.ComplianceRecordNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.compliance.*;
import com.educonnect.model.user.User;
import com.educonnect.repo.compliance.ComplianceRecordRepo;
import com.educonnect.repo.compliance.NoteRepository;
import com.educonnect.service.contract.compliance.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {

    private final ComplianceRecordRepo recordRepository;
    private final NoteRepository noteRepository;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public ComplianceRecordResponseDTO createRecord(ComplianceRecordRequestDTO dto) throws UserNotFoundException {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ComplianceRecord record = new ComplianceRecord();
        record.setUser(user);
        record.setType(dto.getType());
        record.setResult(dto.getResult());

        // Save record first
        ComplianceRecord savedRecord = recordRepository.save(record);

        // Add notes if present
        if (dto.getNotes() != null) {
            dto.getNotes().forEach(text -> {
                Note note = new Note();
                note.setNote(text);
                note.setComplianceRecord(savedRecord);
                noteRepository.save(note); // Persist note
                savedRecord.getNotes().add(note); // Sync memory
            });
        }

        return mapToDTO(savedRecord);
    }

    @Override
    public ComplianceRecordResponseDTO getRecordById(UUID id) {
        ComplianceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ComplianceRecordNotFoundException("Record not found: " + id));

        // Just map the found entity to DTO and return it
        return mapToDTO(record);
    }

    @Override
    public List<ComplianceRecordResponseDTO> getAllRecords() {
        return recordRepository.findAll().stream()
                .map(this::mapToDTO) // Direct mapping
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComplianceRecordResponseDTO updateRecord(UUID id, ComplianceRecordRequestDTO dto) {
        ComplianceRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ComplianceRecordNotFoundException("Cannot update. Record not found: " + id));

        record.setType(dto.getType());
        record.setResult(dto.getResult());

        // Handle Notes
        noteRepository.deleteAll(record.getNotes());
        record.getNotes().clear();

        if (dto.getNotes() != null) {
            dto.getNotes().forEach(text -> {
                Note note = new Note();
                note.setNote(text);
                note.setComplianceRecord(record);
                noteRepository.save(note);
                record.getNotes().add(note); // Keep memory in sync
            });
        }

        ComplianceRecord updated = recordRepository.save(record);
        return mapToDTO(updated); // Use the helper, NOT getRecordById()
    }
    // HELPER METHOD: Converts Entity to DTO safely
    private ComplianceRecordResponseDTO mapToDTO(ComplianceRecord record) {
        ComplianceRecordResponseDTO dto = new ComplianceRecordResponseDTO();
        dto.setComplianceRecordID(record.getComplianceRecordID());
        dto.setUserId(record.getUser().getUserId());
        dto.setType(record.getType());
        dto.setResult(record.getResult());
        dto.setDate(record.getDate());

        if (record.getNotes() != null) {
            dto.setNotes(record.getNotes().stream()
                    .map(Note::getNote)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
