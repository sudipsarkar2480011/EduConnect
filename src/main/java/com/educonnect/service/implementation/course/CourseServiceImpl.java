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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service

/**
 * Implementation of the CourseService interface.
 * <p>This service provides the core business logic for course lifecycle management,
 * including course creation, retrieval, deletion, and the student enrollment process.</p>
 *
 * @author sanchita das
 * @version 1.0
 * @since 1.0
 */

public class CourseServiceImpl implements CourseService {

    @Autowired
    private TeacherRepo teacherRepo;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private StudentRepo studentRepo;

    private final CourseMapper courseMapper =new CourseMapper();

    private  final StudentMapper studentMapper=new StudentMapper();

    /**
     * Creates a new course and assigns a teacher as the instructor.
     *
     * @param request the course details provided in the request body.
     * @param teacher the teacher entity to be associated with this course.
     * @return a CourseResponseDTO containing the saved course details.
     */

    @Override
    public CourseResponseDTO addCourse(CourseRequestDTO request, Teacher teacher) {

        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toResponseDT(savedCourse);

    }

    /**
     * Retrieves all available courses from the database.
     *
     * @return a list of all courses mapped to CourseResponseDTO.
     */

    @Override
    public List<CourseResponseDTO> getAllCourse()
    {
        List<Course> allCourse= courseRepo.findAll();
        return   allCourse.stream().map(courseMapper::toResponseDT).toList();
    }

    /**
     * Finds a specific course by its unique identifier.
     *
     * @param id the UUID of the course.
     * @return the requested course as a CourseResponseDTO.
     * @throws CourseNotFoundException if no course matches the provided ID.
     */

    @Override
    public CourseResponseDTO getByIdCourse(UUID id) throws CourseNotFoundException {
        Course course=courseRepo.findById(id).orElseThrow(()->new CourseNotFoundException("COURSE NOT FOUND WITH THIS ID :"));
        return courseMapper.toResponseDT(course);
    }

    /**
     * Deletes a course from the repository by ID.
     *
     * @param id the UUID of the course to be removed.
     * @return a confirmation string ("Deleted").
     */

    @Override
    public String deleteById(UUID id)
    {
        courseRepo.deleteById(id);
        return "Deleted";
    }

    /**
     * Enrolls a student into a course and initializes their progress tracking.
     * <p>This method is <b>@Transactional</b> to ensure that student records and
     * enrollment records are updated atomically. It sets the initial progress to 0%
     * and the remaining duration to the full course length.</p>
     *
     * @param userId the UUID of the student.
     * @param courseId the UUID of the course.
     * @return a {@link StudentResponse} showing the student's updated enrollment status.
     * @throws IllegalStateException if the student is already enrolled.
     * @throws RuntimeException if the user is not found.
     * @throws CourseNotFoundException if the course is not found.
     */

    @Override
    @Transactional
    public StudentResponse addStudentToCourse(UUID userId, UUID courseId) {
        if (enrollmentRepo.existsByStudentUserIdAndCourseCourseId(userId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Student student = null;
        try {
            student = studentRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User nt found: "));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        Course course = courseRepo.findById(courseId).orElseThrow(()->new CourseNotFoundException("Course not found: "));

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
     * Retrieves all modules (curriculum content) for a specific course.
     *
     * @param courseId the UUID of the course.
     * @return a list of ModuleResponseDTO associated with the course.
     * @throws CourseNotFoundException if the course ID is invalid.
     */

    @Override
    public List<ModuleResponseDTO> getAllModulesOfACourse(UUID courseId) {
        Course c= courseRepo.findById(courseId).orElseThrow(()->new CourseNotFoundException("COURSE NOT FOUND: "));
      return c.getModules().stream().map(courseMapper::getModule).toList();

    }

}
