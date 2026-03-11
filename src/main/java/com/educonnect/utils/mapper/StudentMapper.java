package com.educonnect.utils.mapper;

import com.educonnect.dto.user.UserRequestDTO;
import com.educonnect.model.user.Student;
import com.educonnect.dto.student.StudentResponse;
import com.educonnect.utils.UpdateUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StudentMapper implements Mapper<Student, UserRequestDTO,StudentResponse>{

    @Override
    public Student toEntity(UserRequestDTO requestDTO) {
        return null;
    }

    @Override
    public StudentResponse toResponseDTO(Student s) {
        Objects.requireNonNull(s);
        StudentResponse studentResponse = new StudentResponse();
        UpdateUtil.setIfPresent(s.getUserId(),studentResponse::setUserId);
        UpdateUtil.setIfPresent(s.getFullName(),studentResponse::setFullName);
        UpdateUtil.setIfPresent(s.getEmail(),studentResponse::setEmail);
        UpdateUtil.setIfPresent(s.getRole(),studentResponse::setRole);
        UpdateUtil.setIfPresent(s.isActive(),studentResponse::setActive);
        UpdateUtil.setIfPresent(s.getDateOfBirth(),studentResponse::setDateOfBirth);
        UpdateUtil.setIfPresent(s.getEnrollmentNumber(),studentResponse::setEnrollmentNumber);
        return studentResponse;
    }
}