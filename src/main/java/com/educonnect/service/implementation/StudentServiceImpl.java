package com.educonnect.service.implementation;

import com.educonnect.model.user.Student;
import com.educonnect.model.user.User;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentRegisterRequest;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.utils.mapper.StudentMapper;
import com.educonnect.service.strategy.impl.StudentAuthStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper mapper;
    private final StudentAuthStrategy studentAuthStrategy;

    public StudentServiceImpl(StudentRepo studentRepo,
                              StudentMapper mapper,
                              StudentAuthStrategy studentAuthStrategy) {
        this.studentRepo = studentRepo;
        this.mapper = mapper;
        this.studentAuthStrategy = studentAuthStrategy;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        Student student = studentRepo.findByUserId(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
        return mapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getByUserId(UUID userId) {
        Student student = studentRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found for userId: " + userId));
        return mapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(UUID userId) {
        return studentRepo.existsByUserId(userId);
    }

    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest request) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));

        if (request.getFullName() != null) {
            student.setFullName(request.getFullName());
        }
        if (request.getEmail() != null)  {
            student.setEmail(request.getEmail());
        }
        if (request.getActive() != null)  {
            student.setActive(request.getActive());
        }

        Student saved = studentRepo.save(student);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!studentRepo.existsById(id)) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        studentRepo.deleteById(id);
    }
}