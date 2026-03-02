package com.educonnect.controller;

import com.educonnect.dto.LoginRequestDTO;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.factory.UserFactory;
import com.educonnect.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor

/**
 * REST controller for authentication
 * *
 *  @author santadiprudra
 *  @version 1.0
 *  @since 1.0
 */

public class AuthController
{
    private final UserFactory userFactory;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user){
       return ResponseEntity.ok(userFactory.executeSave(user));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO)
    {
        return ResponseEntity.ok(userFactory.verify(requestDTO));
    }
}
