package com.educonnect.service.implementation.course;

import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.CourseNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.course.Course;
import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.repo.EnrollmentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.utils.mapper.CourseMapper;
import com.educonnect.utils.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing courses within the EduConnect platform.
 * This class handles the business logic for course creation, retrieval,
 * deletion, and student enrollment processes.
 *
 * @author sanchita das
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {


    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;
    private final StudentRepo studentRepo;

    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    /**
     * Creates a new course and assigns it to a specific teacher.
     *
     * @param request The DTO containing course details such as name and duration.
     * @param teacher The {@link Teacher} entity who will be the instructor for this course.
     * @return A {@link CourseResponseDTO} representing the newly created course.
     */
    @Override
    public CourseResponseDTO addCourse(CourseRequestDTO request, Teacher teacher) {
        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toResponseDT(savedCourse);
    }

    /**
     * Retrieves all courses available in the system.
     *
     * @return A list of {@link CourseResponseDTO} objects.
     */
    @Override
    public List<CourseResponseDTO> getAllCourse() {
        List<Course> allCourse = courseRepo.findAll();
        return allCourse.stream().map(courseMapper::toResponseDT).toList();
    }

    /**
     * Finds a specific course by its unique identifier.
     *
     * @param id The {@link UUID} of the course.
     * @return The corresponding {@link CourseResponseDTO}.
     * @throws CourseNotFoundException if no course exists with the given ID.
     */
    @Override
    public CourseResponseDTO getByIdCourse(UUID id) throws CourseNotFoundException {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("COURSE NOT FOUND WITH THIS ID: " + id));
        return courseMapper.toResponseDT(course);
    }

    /**
     * Deletes a course from the database by its ID.
     *
     * @param id The {@link UUID} of the course to be removed.
     * @return A success message string.
     */
    @Override
    public String deleteById(UUID id) {
        courseRepo.deleteById(id);
        return "Deleted";
    }

    /**
     * Enrolls a student into a specific course.
     * This method initializes the enrollment progress, duration, and grades.
     *
     * @param userId   The {@link UUID} of the student.
     * @param courseId The {@link UUID} of the course to enroll in.
     * @return A {@link StudentResponse} DTO showing the updated student state.
     * @throws UserNotFoundException  if the student ID does not exist.
     * @throws CourseNotFoundException if the course ID does not exist.
     * @throws IllegalStateException   if the student is already enrolled in the course.
     */
    @Override
    @Transactional
    public StudentResponse addStudentToCourse(UUID userId, UUID courseId) throws UserNotFoundException {
        if (enrollmentRepo.existsByStudentUserIdAndCourseCourseId(userId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Student student = studentRepo.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        Enrollment e = Enrollment.builder()
                .student(student)
                .course(course)
                .finalGrade(0.0)
                .isActive(true)
                .remainingDuration(course.getDuration())
                .progress(0.0)
                .build();

        if (student.getEnrollments() == null) {
            student.setEnrollments(new ArrayList<>());
        }

        enrollmentRepo.save(e);
        student.getEnrollments().add(e);
        studentRepo.save(student);

        return studentMapper.toResponse(student);
    }

    /**
     * Fetches all modules associated with a specific course.
     *
     * @param courseId The {@link UUID} of the target course.
     * @return A list of {@link ModuleResponseDTO} containing module data.
     * @throws CourseNotFoundException if the course ID does not exist.
     */
    @Override
    public List<ModuleResponseDTO> getAllModulesOfACourse(UUID courseId) {
        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("COURSE NOT FOUND: " + courseId));
        return c.getModules().stream().map(courseMapper::getModule).toList();
    }
}