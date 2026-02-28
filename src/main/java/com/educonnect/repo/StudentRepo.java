package com.educonnect.repo;

import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentUuid(UUID studentUuid);
    boolean existsByStudentUuid(UUID studentUuid);

}
