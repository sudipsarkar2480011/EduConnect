package com.educonnect.service.contract.course;

import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Teacher;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing course-related operations.
 * *
 * @author sanchita das
 * @version 1.0
 * @since 1.0
 */

public interface CourseService{
    /**
     * Creates and persists a new course associated with a specific teacher.
     *
     * @param request the data transfer object containing course details.
     * @param teacher the teacher entity who will own and manage the course.
     * @return a {@link CourseResponseDTO} representing the newly created course.
     */

    CourseResponseDTO addCourse(CourseRequestDTO request, Teacher teacher);
    /**
     * Retrieves a list of all courses currently available in the system.
     *
     * @return a list of {@link CourseResponseDTO} objects.
     */

    public List<CourseResponseDTO> getAllCourse();
    /**
     * Finds a specific course by its unique identifier.
     *
     * @param id the UUID of the course to retrieve.
     * @return the course details if found.
     * @throws Exception if no course exists with the provided ID.
     */
    public CourseResponseDTO getByIdCourse(UUID id) throws Exception;
    /**
     * Removes a course from the system based on its unique ID.
     *
     * @param id the UUID of the course to be deleted.
     * @return a status message confirming the deletion.
     */
    public String deleteById(UUID id);
    /**
     * Enrolls a student into a specific course and initializes their progress tracking.
     *
     * @param userId the UUID of the student user.
     * @param courseId the UUID of the target course for enrollment.
     * @return a {@link StudentResponse} containing the updated student enrollment status.
     */
    StudentResponse addStudentToCourse(UUID userId, UUID courseId);
    /**
     * Retrieves all modules (curriculum content) associated with a specific course.
     *
     * @param courseId the UUID of the course whose modules are being requested.
     * @return a list of {@link ModuleResponseDTO} representing the course's content structure.
     */
List<ModuleResponseDTO> getAllModulesOfACourse(UUID courseId );
}
