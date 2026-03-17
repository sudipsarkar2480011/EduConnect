package com.educonnect.service.contract.course;

import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.exception.custom_exceptions.UserIdDoNothMatchException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Teacher;

import java.util.List;
import java.util.UUID;

public interface CourseService{
    CourseResponseDTO addCourse(CourseRequestDTO request, Teacher teacher);
    public List<CourseResponseDTO> getAllCourse();
    public CourseResponseDTO getByIdCourse(UUID id) throws Exception;
    public String deleteById(UUID id);
    StudentResponse addStudentToCourse( UUID courseId, UUID studentId)  throws UserNotFoundException, UserIdDoNothMatchException;
List<ModuleResponseDTO> getAllModulesOfACourse(UUID courseId );
}
