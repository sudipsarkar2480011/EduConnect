package com.educonnect.service.implementation;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.utils.UpdateUtil;
import com.educonnect.utils.mapper.StudentMapper;
import com.educonnect.service.strategy.impl.StudentAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper mapper;
    private final StudentAuthStrategy studentAuthStrategy;

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        Student student = studentRepo.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found: " + id));
        return mapper.toResponseDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepo.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest request) throws UserNotFoundException {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Student not found: " + id));

        UpdateUtil.setIfPresent(request.getFullName(),student::setFullName);
        UpdateUtil.setIfPresent(request.getEmail(),student::setEmail);
        UpdateUtil.setIfPresent(request.getDateOfBirth(),student::setDateOfBirth);
        UpdateUtil.setIfPresent(request.getActive(),student::setActive);
        UpdateUtil.setIfPresent(request.getEnrollmentNumber(),student::setEnrollmentNumber);
        return mapper.toResponseDTO(studentRepo.save(student));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        if (!studentRepo.existsById(id)) {
            throw new UsernameNotFoundException("Student not found: " + id);
        }
        studentRepo.deleteById(id);
    }
}