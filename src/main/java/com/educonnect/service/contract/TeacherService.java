package com.educonnect.service.contract;

import com.educonnect.dto.TeacherCreateDTO;
import com.educonnect.dto.TeacherResponseDTO;
import com.educonnect.dto.TeacherUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface TeacherService {
    TeacherResponseDTO create(TeacherCreateDTO dto);
    TeacherResponseDTO getById(UUID id);
    Page<TeacherResponseDTO> getAll(Pageable pageable);
    TeacherResponseDTO update(UUID id, TeacherUpdateDTO dto);
    void delete(UUID id);


}
