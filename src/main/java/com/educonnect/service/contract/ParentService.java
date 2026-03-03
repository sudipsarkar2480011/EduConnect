package com.educonnect.service.contract;

import com.educonnect.dto.parent.ParentCreateDTO;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.model.user.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ParentService {

    ParentResponseDTO create(ParentCreateDTO dto);

    ParentResponseDTO getById(UUID id);

    Page<ParentResponseDTO> getAll(Pageable pageable);

    ParentResponseDTO update(UUID id, ParentUpdateDTO dto);

    void delete(UUID id);

    // Supports linking a Parent to a Student as used in ParentController
    ParentResponseDTO linkStudent(UUID parentId, UUID studentId);

    interface AdminService {
        Admin create(Admin admin);
        Admin getById(UUID id);
        List<Admin> getAll();
        Admin update(UUID id, Admin admin);
        void changePassword(UUID id, String newRawPassword);
        void delete(UUID id);
    }
}