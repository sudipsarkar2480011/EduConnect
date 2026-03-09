package com.educonnect.repo;

import com.educonnect.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, UUID> {

    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Teacher> findByFullName(String fullName);


}
