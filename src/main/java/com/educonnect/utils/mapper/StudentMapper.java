package com.educonnect.utils.mapper;

import com.educonnect.model.user.Student;
import com.educonnect.dto.student.StudentResponse;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

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
}