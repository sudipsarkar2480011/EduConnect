package com.educonnect.service.implementation.course;

import com.educonnect.model.course.Course;
import com.educonnect.model.user.Teacher;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.course.CourseService;
import com.educonnect.service.contract.course.CourseVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepo repo;

    private TeacherRepo teacherRepo;

    @Autowired
    private CourseVideoService courseVideoService;

    @Override
    public Course addCourse(Course course)
    {
        UUID teacherId = course.getTeacher().getUserId();
        Teacher managedTeacher = (Teacher) teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        course.setTeacher(managedTeacher);

        return repo.save(course);

    }

    @Override
    public List<Course> getAllCourse()
    {
        return repo.findAll();
    }

    @Override
    public Course getByIdCourse(UUID id) throws Exception {
        return repo.findById(id).orElseThrow(()->new Exception("no course present ; "));
    }
    @Override
    public String deleteById(UUID id)
    {
        repo.deleteById(id);
        return "Deleted";
    }

}
