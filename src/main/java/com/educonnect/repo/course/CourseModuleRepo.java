package com.educonnect.repo.course;

import com.educonnect.model.course.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseModuleRepo extends JpaRepository<CourseModule, UUID> {
//    List<CourseModule> findAllByCourseCourseId(UUID courseId);
}
