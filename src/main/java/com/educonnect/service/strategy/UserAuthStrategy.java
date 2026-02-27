package com.educonnect.service.strategy;

import com.educonnect.model.user.User;

public interface UserAuthStrategy {
    boolean supports(String role);
    User save(User u);
}
