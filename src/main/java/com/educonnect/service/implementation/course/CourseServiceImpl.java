package com.educonnect.service.implementation.course;

import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.CourseNotFoundException;
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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
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
    @Override
    public CourseResponseDTO addCourse(CourseRequestDTO request) {
        Teacher teacher = teacherRepo.findById(request.teacherId())
                .orElseThrow(() -> new UsernameNotFoundException("TEACHER NOT FOUND: "));
        Course course = courseMapper.toEntity(request);
        course.setTeacher(teacher);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toResponseDT(savedCourse);

    }

    @Override
    public List<CourseResponseDTO> getAllCourse()
    {
        List<Course> allCourse= courseRepo.findAll();
        return   allCourse.stream().map(courseMapper::toResponseDT).toList();
    }

    @Override
    public CourseResponseDTO getByIdCourse(UUID id) throws CourseNotFoundException {
        Course course=courseRepo.findById(id).orElseThrow(()->new CourseNotFoundException("COURSE NOT FOUND WITH THIS ID :"));
        return courseMapper.toResponseDT(course);
    }
    @Override
    public String deleteById(UUID id)
    {
        courseRepo.deleteById(id);
        return "Deleted";
    }

    @Override
    @Transactional
    public StudentResponse addStudentToCourse(UUID userId, UUID courseId) {
        if (enrollmentRepo.existsByStudentUserIdAndCourseCourseId(userId, courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Student student = studentRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException("User nt found: "));
        Course course = courseRepo.findById(courseId).orElseThrow(()->new CourseNotFoundException("Course not found: "));

        Enrollment e = Enrollment.builder()
                .student(student)
                .course(course)
                .finalGrade(0.0)
                .isActive(true)
                .build();
        if (student.getEnrollments() == null) {
            student.setEnrollments(new ArrayList<>());
        }
        student.getEnrollments().add(e);
        studentRepo.save(student);
        return studentMapper.toResponse(student);
    }
    @Override
    public List<ModuleResponseDTO> getAllModulesOfACourse(UUID courseId) {
        Course c= courseRepo.findById(courseId).orElseThrow(()->new CourseNotFoundException("COURSE NOT FOUND: "));
      return c.getModules().stream().map(courseMapper::getModule).toList();

    }


}
