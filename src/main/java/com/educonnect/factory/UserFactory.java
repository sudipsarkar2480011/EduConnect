package com.educonnect.factory;

import com.educonnect.model.user.User;
import com.educonnect.service.strategy.UserAuthStrategy;
import com.educonnect.utils.UserValidation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFactory
{
    private final List<UserAuthStrategy> strategyList;

    public  UserFactory(List<UserAuthStrategy>  strategyList){
        this.strategyList=strategyList;
    }

    public User executeSave(User u){
        UserValidation.EMAIL_VALID
                .and(UserValidation.PASSWORD_LENGTH)
                .and(UserValidation.PASSWORD_STRENGTH)
                .validateOrThrow(u);

        return  strategyList.stream()
                .filter(s->s.supports(u.getRole().toString()))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Role not supported"))
                .save(u);
    }
}
