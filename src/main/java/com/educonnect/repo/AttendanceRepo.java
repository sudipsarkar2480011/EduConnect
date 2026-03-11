package com.educonnect.repo;

import com.educonnect.model.attendance.Attendance;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceRepo extends JpaRepository<Attendance, UUID> {
      Boolean existsByStudentAndCourseAndDate(Student s, Course c, LocalDate date);
      List<Attendance> findByStudentUserId(UUID studentId);
      List<Attendance> findByCourseCourseId(UUID courseId);

      @Query("Select a from Attendance a where a.student.userId= :studentId AND a.course.courseId= :courseId")
      List<Attendance> findByCourseAndAttendance(@Param("studentId") UUID studentId, @Param("courseId") UUID courseId);

}
