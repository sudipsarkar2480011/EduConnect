package com.educonnect.service.contract;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.exception.custom_exceptions.NoChildFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;

import java.util.UUID;

public interface ParentService {
    ParentResponseDTO getById(UUID id) throws UserNotFoundException;
    ParentResponseDTO update(UUID id, ParentUpdateDTO dto) throws UserNotFoundException;
    void delete(UUID id) throws UserNotFoundException;
    ParentResponseDTO linkStudent(UUID parentId, UUID studentId) throws NoChildFoundException;
}