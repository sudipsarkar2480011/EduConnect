package com.educonnect.repo;

import com.educonnect.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepo extends JpaRepository<Teacher, UUID> {

    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Teacher> findByFullName(String fullName);


}
