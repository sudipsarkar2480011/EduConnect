package com.educonnect.service.strategy.impl;

import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.User;
import com.educonnect.repo.ParentRepo;
import com.educonnect.service.strategy.UserAuthStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentAuthStrategy  implements UserAuthStrategy {

    private final ParentRepo parentRepo;

    @Override
    public boolean supports(String role) {
        return "PARENT".equalsIgnoreCase(role);
    }

    @Override
    public User save(User u) {
        return parentRepo.save(Parent.builder()
                .fullName(u.getFullName())
                .email(u.getEmail())
                .password(u.getPassword())
                .role(Role.PARENT)
                .build());
    }
}
