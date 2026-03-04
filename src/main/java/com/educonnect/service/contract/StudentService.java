package com.educonnect.service.contract;

import com.educonnect.dto.student.StudentRegisterRequest;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponse getById(UUID id);
    List<StudentResponse> getAll();
    StudentResponse getByUserId(UUID userId);
    boolean existsByUserId(UUID userId);

    // Create via registration pipeline (hash password, set role)
    StudentResponse register(StudentRegisterRequest request);

    StudentResponse update(UUID id, StudentUpdateRequest request);
    void delete(UUID id);

    StudentResponse getByEmail(String email);
    List<StudentResponse> findByFullName(String fullName);

}