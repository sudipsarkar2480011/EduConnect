package com.educonnect.service.contract;

import com.educonnect.dto.parent.ParentCreateDTO;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface ParentService {

    ParentResponseDTO create(ParentCreateDTO dto);

    ParentResponseDTO getById(UUID id);

    Page<ParentResponseDTO> getAll(Pageable pageable);

    ParentResponseDTO update(UUID id, ParentUpdateDTO dto);

    void delete(UUID id);

    // Supports linking a Parent to a Student as used in ParentController
    ParentResponseDTO linkStudent(UUID parentId, UUID studentId);
}