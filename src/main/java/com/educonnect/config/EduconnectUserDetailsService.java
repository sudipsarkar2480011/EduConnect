package com.educonnect.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.educonnect.model.user.User;

@Service
@RequiredArgsConstructor
public class EduconnectUserDetailsService implements UserDetailsService {
    private final UserRepo repo ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email).orElseThrow();
        return new UserPrinciples(user);
    }
}
