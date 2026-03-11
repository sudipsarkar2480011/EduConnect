package com.educonnect.service.contract;

import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponse getById(UUID id) throws UserNotFoundException;
    List<StudentResponse> getAll();
    StudentResponse update(UUID id, StudentUpdateRequest request) throws UserNotFoundException;
    void delete(UUID id) throws UserNotFoundException;
}