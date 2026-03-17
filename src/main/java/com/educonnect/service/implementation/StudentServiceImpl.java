package com.educonnect.service.implementation;

import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.service.contract.parent.ParentService;
import com.educonnect.utils.UpdateUtil;
import com.educonnect.utils.mapper.StudentMapper;
import com.educonnect.service.strategy.impl.StudentAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing {@link Student} entities.
 * handles the business logic for retrieving, updating, and deleting students,
 * ensuring data consistency through Spring's transaction management.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final ParentService parentService;
    private final StudentRepo studentRepo;
    private final StudentMapper mapper;
    private final StudentAuthStrategy studentAuthStrategy;

    /**
     * Retrieves a specific student by their unique user identifier.
     *
     * @param id The {@link UUID} of the student to retrieve.
     * @return A {@link StudentResponse} containing the student data.
     * @throws UsernameNotFoundException if no student is found with the provided ID.
     */
    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        Student student = studentRepo.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found: " + id));
        return mapper.toResponseDTO(student);
    }

    /**
     * Retrieves a list of all students registered in the system.
     *
     * @return A {@link List} of {@link StudentResponse} objects.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepo.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * Updates an existing student's information based on the provided request data.
     * Only fields present in the request will be updated (partial update).
     *
     * @param id      The {@link UUID} of the student to update.
     * @param request The {@link StudentUpdateRequest} containing the new values.
     * @return The updated {@link StudentResponse}.
     * @throws UserNotFoundException if the student does not exist in the repository.
     */
    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest request) throws UserNotFoundException {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Student not found: " + id));

        UpdateUtil.setIfPresent(request.getFullName(), student::setFullName);
        UpdateUtil.setIfPresent(request.getEmail(), student::setEmail);
        UpdateUtil.setIfPresent(request.getDateOfBirth(), student::setDateOfBirth);
        UpdateUtil.setIfPresent(request.getActive(), student::setActive);
        UpdateUtil.setIfPresent(request.getEnrollmentNumber(), student::setEnrollmentNumber);
        UpdateUtil.setIfPresent(request.getParentEmail(),student::setParentEmail);
        return mapper.toResponseDTO(studentRepo.save(student));
    }

    /**
     * Deletes a student from the system by their ID.
     *
     * @param id The {@link UUID} of the student to be removed.
     * @throws UsernameNotFoundException if the student ID does not exist.
     */
    @Transactional
    @Override
    public void delete(UUID id) {
        if (!studentRepo.existsById(id)) {
            throw new UsernameNotFoundException("Student not found: " + id);
        }
        studentRepo.deleteById(id);
    }
}