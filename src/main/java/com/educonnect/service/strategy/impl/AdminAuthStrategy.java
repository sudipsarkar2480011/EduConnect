package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.AdminRepo;
import com.educonnect.service.strategy.UserAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthStrategy implements UserAuthStrategy {

    private final AdminRepo adminRepo;

    @Override
    public boolean supports(String role) {
        return "ADMIN".equalsIgnoreCase(role);
    }

    @Override
    public User save(User u) {
        return adminRepo.save(Admin.builder()
                .fullName(u.getFullName())
                .email(u.getEmail())
                .password(u.getPassword())
                .role(Role.ADMIN)
                .build());
    }
}
