package com.educonnect.service.contract;

import com.educonnect.dto.teacher.TeacherCreateDTO;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.teacher.TeacherUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface TeacherService {
    TeacherResponseDTO create(TeacherCreateDTO dto);
    TeacherResponseDTO getById(UUID id);
    Page<TeacherResponseDTO> getAll(Pageable pageable);
    TeacherResponseDTO update(UUID id, TeacherUpdateDTO dto);
    void delete(UUID id);
}
