package com.educonnect.repo.course;

import com.educonnect.model.course.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface CourseModuleRepo extends JpaRepository<CourseModule, UUID> {
}
