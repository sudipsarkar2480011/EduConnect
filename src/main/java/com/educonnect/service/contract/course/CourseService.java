package com.educonnect.service.contract.course;

import com.educonnect.model.course.Course;

import java.util.List;
import java.util.UUID;

public interface CourseService{
    Course addCourse(Course course);
    public List<Course> getAllCourse();
    public Course getByIdCourse(UUID id) throws Exception;
    public String deleteById(UUID id);

}
