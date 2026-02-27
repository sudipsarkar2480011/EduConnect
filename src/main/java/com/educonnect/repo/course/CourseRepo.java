package com.educonnect.repo.course;

import com.educonnect.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepo extends JpaRepository<Course, UUID> {
}
