package com.educonnect.service.contract;

import com.educonnect.dto.teacher.TeacherCreateDTO;
import com.educonnect.dto.teacher.TeacherResponseDTO;
import com.educonnect.dto.teacher.TeacherUpdateDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface TeacherService {
    TeacherResponseDTO getById(UUID id) throws UserNotFoundException;
    Page<TeacherResponseDTO> getAll(Pageable pageable);
    TeacherResponseDTO update(UUID id, TeacherUpdateDTO dto) throws UserNotFoundException;
    void delete(UUID id) throws UserNotFoundException;


}
