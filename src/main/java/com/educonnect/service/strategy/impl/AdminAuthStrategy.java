package com.educonnect.service.strategy.impl;

import com.educonnect.config.JWTService;
import com.educonnect.dto.LoginRequestDTO;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.AdminRepo;
import com.educonnect.service.strategy.UserAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthStrategy implements UserAuthStrategy {

    private final AdminRepo adminRepo;

    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean supports(String role) {
        return "ADMIN".equalsIgnoreCase(role);
    }

    @Override
    public User save(User u) {
        return adminRepo.save(Admin.builder()
                .fullName(u.getFullName())
                .email(u.getEmail())
                .password(encoder.encode(u.getPassword()))
                .role(Role.ADMIN)
                .build());
    }
}
