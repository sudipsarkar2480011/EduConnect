package com.educonnect.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< Updated upstream:src/main/java/com/educonnect/dto/LoginResponseDTO.java
public class LoginResponseDTO {
    private String token;
=======
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private UUID uuid;
    private String accessToken;
    private String refreshToken;
>>>>>>> Stashed changes:src/main/java/com/educonnect/dto/user/UserResponseDTO.java
    private String email;
    private String role;
    private String name;
}