package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Role;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.User;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.strategy.UserAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentAuthStrategy implements UserAuthStrategy {


    private final StudentRepo studentRepo;

    @Override
    public boolean supports(String role) {
        return "STUDENT".equalsIgnoreCase(role);
    }

    @Override
    public User save(User u) {
        return studentRepo.save(Student.builder()
                        .fullName(u.getFullName())
                        .email(u.getEmail())
                        .password(u.getPassword())
                        .role(Role.STUDENT)
                .build());
    }
}
