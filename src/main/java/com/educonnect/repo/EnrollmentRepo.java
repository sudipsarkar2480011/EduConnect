package com.educonnect.repo;

import com.educonnect.model.course.Course;
import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface EnrollmentRepo extends JpaRepository<Enrollment, UUID> {

    boolean existsByStudentUserIdAndCourseCourseId(UUID userId, UUID courseId);
}
