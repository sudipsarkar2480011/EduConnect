package com.educonnect.repo;

import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepo extends JpaRepository<Student, UUID> {

    Optional<Student> findByUserId(UUID studentId);
    boolean existsByUserId(UUID StudentId);

}
