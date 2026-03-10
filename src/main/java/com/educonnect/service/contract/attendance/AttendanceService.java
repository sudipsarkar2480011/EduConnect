package com.educonnect.service.contract.attendance;

import com.educonnect.exception.custom_exceptions.CourseNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.attendance.Attendance;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing and retrieving student attendance records.
 * Provides functionality to log new attendance and query existing records
 * by student, course, or a combination of both.
 *
 * @author Santadip Rudra
 * @version 1.0
 * @since 2026
 */
public interface AttendanceService {

    /**
     * Records a new attendance entry for a specific student in a specific course.
     *
     * @param studentId The unique identifier (UUID) of the student.
     * @param courseId  The unique identifier (UUID) of the course.
     * @return {@code true} if the attendance was successfully recorded, {@code false} otherwise.
     * @throws CourseNotFoundException if no course exists with the provided {@code courseId}.
     * @throws UserNotFoundException   if no student exists with the provided {@code studentId}.
     */
    boolean addAttendance(UUID studentId, UUID courseId) throws CourseNotFoundException, UserNotFoundException;

    /**
     * Retrieves all attendance records associated with a specific student.
     *
     * @param studentId The unique identifier (UUID) of the student.
     * @return A {@link List} of {@link Attendance} objects belonging to the student.
     * Returns an empty list if no records are found.
     */
    List<Attendance> findByStudentId(UUID studentId);

    /**
     * Retrieves all attendance records associated with a specific course.
     *
     * @param courseId The unique identifier (UUID) of the course.
     * @return A {@link List} of {@link Attendance} objects registered for the course.
     * Returns an empty list if no records are found.
     */
    List<Attendance> findByCourseId(UUID courseId);

    /**
     * Retrieves attendance records for a specific student within a specific course.
     * Use this to track a student's individual progress or history in a single subject.
     *
     * @param studentId The unique identifier (UUID) of the student.
     * @param courseId  The unique identifier (UUID) of the course.
     * @return A {@link List} of {@link Attendance} objects matching both criteria.
     */
    List<Attendance> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
}