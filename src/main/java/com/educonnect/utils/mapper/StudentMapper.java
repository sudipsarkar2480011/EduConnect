package com.educonnect.utils.mapper;

import com.educonnect.dto.user.UserRequestDTO;
import com.educonnect.model.user.Student;
import com.educonnect.dto.student.StudentResponse;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper implements Mapper<Student, UserRequestDTO,StudentResponse>{
    public StudentResponse toResponse(Student s) {
        if (s == null) return null;
        return new StudentResponse(
                s.getUserId(),     // inherited from User
                s.getFullName(),
                s.getEmail(),
                s.getRole(),
                s.isActive(),
                s.getEnrollments()
        );
    }

    @Override
    public Student toEntity(UserRequestDTO requestDTO) {
        return null;
    }

    @Override
    public StudentResponse toResponseDT(Student entity) {
        if (entity == null) return null;
        return new StudentResponse(
                entity.getUserId(),     // inherited from User
                entity.getFullName(),
                entity.getEmail(),
                entity.getRole(),
                entity.isActive(),
                entity.getEnrollments()
        );
    }
}