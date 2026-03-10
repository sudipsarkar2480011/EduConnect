package com.educonnect.service.implementation;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentService;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.dto.student.StudentUpdateRequest;
import com.educonnect.utils.mapper.StudentMapper;
import com.educonnect.service.strategy.impl.StudentAuthStrategy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link StudentService} interface.
 * <p>This service handles the core business logic for student profile management,
 * including data retrieval, partial updates, and deletion. It utilizes a
 * {@link StudentAuthStrategy} for authentication-related concerns.</p>
 *
 * @author harini
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper mapper;
    private final StudentAuthStrategy studentAuthStrategy;

    /**
     * Constructs a new StudentServiceImpl with required dependencies.
     *
     * @param studentRepo the repository for database operations.
     * @param mapper the utility for converting entities to DTOs.
     * @param studentAuthStrategy the strategy for student-specific authentication.
     */
    public StudentServiceImpl(StudentRepo studentRepo,
                              StudentMapper mapper,
                              StudentAuthStrategy studentAuthStrategy) {
        this.studentRepo = studentRepo;
        this.mapper = mapper;
        this.studentAuthStrategy = studentAuthStrategy;
    }

    /**
     * Retrieves a student by their unique user identifier.
     *
     * @param id the UUID of the student user.
     * @return a {@link StudentResponse} containing the profile data.
     * @throws UsernameNotFoundException if no student is found with the given ID.
     */
    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(UUID id) {
        Student student = studentRepo.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found: " + id));
        return mapper.toResponse(student);
    }

    /**
     * Fetches all registered students from the system.
     *
     * @return a list of {@link StudentResponse} DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Performs a partial update on a student's profile information.
     * <p>Only non-null fields in the request will overwrite existing student data.</p>
     *
     * @param id the UUID of the student to update.
     * @param request the DTO containing the update values.
     * @return the updated {@link StudentResponse}.
     * @throws UserNotFoundException if the student does not exist.
     */
    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest request) throws UserNotFoundException {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Student not found: " + id));

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

    /**
     * Permanently removes a student record from the system.
     *
     * @param id the UUID of the student to delete.
     * @throws UsernameNotFoundException if the student is not found in the database.
     */
    @Override
    public void delete(UUID id) {
        if (!studentRepo.existsById(id)) {
            throw new UsernameNotFoundException("Student not found: " + id);
        }
        studentRepo.deleteById(id);
    }
}