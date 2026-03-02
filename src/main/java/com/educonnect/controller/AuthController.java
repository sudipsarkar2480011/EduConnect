package com.educonnect.controller;

import com.educonnect.dto.auth.AuthResponse;
import com.educonnect.dto.user.UserRequestDTO;
import com.educonnect.dto.user.UserResponseDTO;
import com.educonnect.factory.UserFactory;
import com.educonnect.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for authentication
 * *
 *  @author santadiprudra
 *  @version 1.0
 *  @since 1.0
 */
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final UserFactory userFactory;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user){
       User u = userFactory.executeSave(user);
       return ResponseEntity.ok(AuthResponse.builder()
                       .message("success")
                       .data(u.getEmail())
                       .status(true)
               .build());
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequestDTO requestDTO)
    {
        var u = userFactory.verify(requestDTO);
        return ResponseEntity.ok( AuthResponse.builder()
                        .token(u.getToken())
                        .message("success")
                        .status(true)
                        .build());
    }
}
