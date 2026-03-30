package com.educonnect.repo.course;

import com.educonnect.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepo extends JpaRepository<Course, UUID> {
  //  @Query("SELECT c FROM Course c LEFT JOIN FETCH c.modules WHERE c.courseId = :courseId")
 //   Optional<Course> findByIdWithModules(@Param("courseId") UUID courseId);
}
