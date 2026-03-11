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

/**
 * Repository interface for {@link Attendance} entities.
 * Provides abstraction for database operations related to tracking student attendance in courses.
 */
public interface AttendanceRepo extends JpaRepository<Attendance, UUID> {

    /**
     * Checks if an attendance record already exists for a specific student, course, and date.
     * Useful for preventing duplicate attendance entries for the same day.
     *
     * @param s    The {@link Student} entity.
     * @param c    The {@link Course} entity.
     * @param date The {@link LocalDate} of the class session.
     * @return true if a record exists, false otherwise.
     */
    Boolean existsByStudentAndCourseAndDate(Student s, Course c, LocalDate date);

    /**
     * Retrieves all attendance records associated with a specific student.
     *
     * @param studentId The unique {@link UUID} of the student user.
     * @return A list of {@link Attendance} records for the given student.
     */
    List<Attendance> findByStudentUserId(UUID studentId);

    /**
     * Retrieves all attendance records for a specific course.
     *
     * @param courseId The unique {@link UUID} of the course.
     * @return A list of all {@link Attendance} entries for the given course.
     */
    List<Attendance> findByCourseCourseId(UUID courseId);

    /**
     * Retrieves a filtered list of attendance records for a specific student within a specific course.
     *
     * @param studentId The {@link UUID} of the student.
     * @param courseId  The {@link UUID} of the course.
     * @return A list of {@link Attendance} records matching both criteria.
     */
    @Query("Select a from Attendance a where a.student.userId = :studentId AND a.course.courseId = :courseId")
    List<Attendance> findByCourseAndAttendance(@Param("studentId") UUID studentId, @Param("courseId") UUID courseId);

}