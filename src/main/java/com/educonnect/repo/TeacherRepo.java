package com.educonnect.repo;

import com.educonnect.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeacherRepo extends JpaRepository<Teacher, UUID> {
}
