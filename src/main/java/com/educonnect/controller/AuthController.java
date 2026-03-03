package com.educonnect.controller;

import com.educonnect.config.JWTService;
import com.educonnect.dto.user.UserRequestDTO;
import com.educonnect.dto.user.UserResponseDTO;
import com.educonnect.factory.UserFactory;
import com.educonnect.model.token.RefreshToken;
import com.educonnect.model.user.User;
import com.educonnect.service.contract.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
public class AuthController {
    private final UserFactory userFactory;
    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {
        User u = userFactory.executeSave(user);
        return ResponseEntity.ok(UserResponseDTO.builder()
                .email(u.getEmail())
                .build());
    }

    @PostMapping("login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO requestDTO) {
        return ResponseEntity.ok(userFactory.verify(requestDTO));
    }

    @SneakyThrows
    @PostMapping("refresh")
    public ResponseEntity<UserResponseDTO> refresh(@RequestBody RefreshToken request) { // Use a DTO for input

        RefreshToken tokenEntity = refreshTokenService.findByToken(request)
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));
        refreshTokenService.verifyToken(tokenEntity);
        User user = tokenEntity.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());

        UserResponseDTO dto = UserResponseDTO.builder()
                .refreshToken(tokenEntity.getToken())
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok(dto);

    }
}
