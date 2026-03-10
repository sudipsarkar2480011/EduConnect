package com.educonnect.service.implementation.attendance;

import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.CourseNotFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.attendance.Attendance;
import com.educonnect.model.attendance.AttendanceStatus;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import com.educonnect.repo.AttendanceRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.StudentService;
import com.educonnect.service.contract.attendance.AttendanceService;
import com.educonnect.service.contract.course.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author  Santadip Rudra
 * @version  1.0
 * @since  2026
 * @apiNote  Implementation class for the {@link  AttendanceService} interface for
 * providing business logic for attendance tracking
 * */
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {


    private final AttendanceRepo attendanceRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    /**
     * Records attendance for a student. If attendance for the current date
     * already exists, it returns true without creating a duplicate.
     *
     * @param studentId Unique identifier of the student.
     * @param courseId  Unique identifier of the course.
     * @return true if attendance is recorded or already exists.
     * @throws UserNotFoundException   if the student does not exist.
     * @throws CourseNotFoundException if the course does not exist.
     */
    @Override
    @Transactional
    public boolean addAttendance
            (
             UUID studentId,
             UUID courseId )
            throws CourseNotFoundException, UserNotFoundException
    {
       Student s = studentRepo.findByUserId(studentId).orElseThrow(()-> new UserNotFoundException(studentId+" not found")
       );
       Course c = courseRepo.findById(courseId).orElseThrow(()->new CourseNotFoundException("Course "+ courseId+ " not found")
        );
       if(attendanceRepo.existsByStudentAndCourseAndDate(s,c,LocalDate.now())){
            return  true;
       }
       Attendance a =  Attendance.builder()
                .student(s)
                .course(c)
                .date(LocalDate.now())
                .status(AttendanceStatus.PRESENT)
               .build();

       attendanceRepo.save(a);
       return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> findByStudentId(UUID studentId) {
        return attendanceRepo.findByStudentUserId(studentId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> findByCourseId(UUID courseId) {
        return attendanceRepo.findByCourseCourseId(courseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Attendance> findByStudentIdAndCourseId(UUID studentId, UUID courseId) {
        return attendanceRepo.findByCourseAndAttendance(studentId,courseId);
    }
}
