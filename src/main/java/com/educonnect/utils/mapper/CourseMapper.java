package com.educonnect.utils.mapper;

import com.educonnect.dto.course.CourseRequestDTO;
import com.educonnect.dto.course.CourseResponseDTO;
import com.educonnect.dto.course.ModuleResponseDTO;
import com.educonnect.model.course.Course;
import com.educonnect.model.course.CourseModule;

public class CourseMapper implements Mapper<Course, CourseRequestDTO, CourseResponseDTO>{

    @Override
    public Course toEntity(CourseRequestDTO requestDTO) {
        Course course=new Course();
        course.setTitle(requestDTO.title());
        course.setDescription(requestDTO.description());
        course.setCourseCode(requestDTO.courseCode());
        course.setDuration(0.0);
        return course;
    }

    @Override
    public CourseResponseDTO toResponseDT(Course entity) {
        return new CourseResponseDTO(
                entity.getCourseId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCourseCode(),
                entity.getDuration(),
                entity.getTeacher().getUserId(),
                entity.getTeacher().getFullName()
        );
    }

    public ModuleResponseDTO getModule(CourseModule courseModule)
    {
        return new ModuleResponseDTO(
                courseModule.getContentUrl()
                ,courseModule.getModuleId(),
                courseModule.getTitle()
                ,courseModule.getDuration(),
                courseModule.getSequenceOrder());
    }
}
