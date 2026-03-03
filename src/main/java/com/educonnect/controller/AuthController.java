package com.educonnect.controller;

import com.educonnect.dto.LoginRequestDTO;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.factory.UserFactory;
<<<<<<< Updated upstream
import com.educonnect.model.user.User;
=======
import com.educonnect.model.audit.Action;
import com.educonnect.model.token.RefreshToken;
import com.educonnect.model.user.User;
import com.educonnect.service.contract.RefreshTokenService;
import com.educonnect.service.contract.audit.AuditLogService;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;
    private final AuditLogService auditLogService;
>>>>>>> Stashed changes

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody User user){
       return ResponseEntity.ok(userFactory.executeSave(user));
    }

    @PostMapping("login")
<<<<<<< Updated upstream
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO)
    {
        return ResponseEntity.ok(userFactory.verify(requestDTO));
    }
=======
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO requestDTO) throws Exception {
        try{
            UserResponseDTO verified = userFactory.verify(requestDTO);
            auditLogService.createAudit(verified.getUuid(), Action.LOGIN,requestDTO.getRole());
            return ResponseEntity.ok(verified);
        }catch (RuntimeException re){
            throw new Exception(re.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("refresh")
    public ResponseEntity<UserResponseDTO> refresh(@RequestBody RefreshToken request) { // Use a DTO for input
        RefreshToken tokenEntity = refreshTokenService.findByToken(request)
                .orElseThrow(() -> new Exception("Refresh token not found in database"));
        refreshTokenService.verifyToken(tokenEntity);
        User user = tokenEntity.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());

        UserResponseDTO dto = UserResponseDTO.builder()
                .refreshToken(tokenEntity.getToken())
                .accessToken(accessToken)
                .build();

        return ResponseEntity.ok(dto);

    }
>>>>>>> Stashed changes
}
