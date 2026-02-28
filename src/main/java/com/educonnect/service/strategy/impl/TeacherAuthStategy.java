package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Role;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.TeacherRepo;
import com.educonnect.service.strategy.UserAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherAuthStategy implements UserAuthStrategy {

    private final TeacherRepo teacherRepo;
    @Override
    public boolean supports(String role) {
        return "TEACHER".equalsIgnoreCase(role);
    }

    @Override
    public User save(User u) {
        return teacherRepo.save(Teacher.builder()
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .password(encoder.encode(u.getPassword()))
                        .role(Role.TEACHER)
                .build());
    }
}
