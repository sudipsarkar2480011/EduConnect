package com.educonnect.service.implementation.auth;

import com.educonnect.model.user.User;
import com.educonnect.service.contract.auth.AuthService;
import com.educonnect.service.strategy.UserAuthStrategy;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public User register(User u) {
        return null;
    }
}
