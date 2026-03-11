package com.educonnect.controller;

import com.educonnect.config.JWTService;
import com.educonnect.dto.user.UserRequestDTO;
import com.educonnect.dto.user.UserResponseDTO;
import com.educonnect.factory.UserFactory;
import com.educonnect.model.token.RefreshToken;
import com.educonnect.model.user.User;
import com.educonnect.service.contract.RefreshTokenService;
import com.educonnect.service.contract.audit.AuditLogService;
import com.educonnect.service.contract.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @TestConfiguration
    static class TestJacksonConfig {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private com.educonnect.config.EduconnectUserDetailsService educonnectUserDetailsService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private AuditLogService auditLogService;

    @MockitoBean
    private UserFactory userFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /v1/auth/register - Success")
    void register_ShouldReturnUser_WhenValidRequest() throws Exception {
        // Arrange
        User inputUser = User.builder()
                .email("student@edu.com")
                .password("StrongPass123!")
                .fullName("John Doe")
                .build();

        User savedUser = User.builder()
                .email("student@edu.com")
                .build();

        when(userFactory.executeSave(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                // Matches the actual controller logic which only returns the email
                .andExpect(jsonPath("$.email").value("student@edu.com"));
    }

    @Test
    @DisplayName("POST /v1/auth/login - Success")
    void login_ShouldReturnResponseDTO_WhenCredentialsAreValid() throws Exception {
        // Arrange
        UserRequestDTO request = UserRequestDTO.builder()
                .email("admin@edu.com")
                .password("adminPass")
                .role("ADMIN")
                .build();

        UUID mockUuid = UUID.randomUUID();
        UserResponseDTO response = UserResponseDTO.builder()
                .uuid(mockUuid)
                .refreshToken("mocked-refresh-token")
                .accessToken("mocked-access-token")
                .email("admin@edu.com")
                .role("ADMIN")
                .name("Admin User")
                .build();

        when(userFactory.verify(any(UserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.refreshToken").value("mocked-refresh-token"))
                .andExpect(jsonPath("$.accessToken").value("mocked-access-token"))
                .andExpect(jsonPath("$.email").value("admin@edu.com"));
    }
    @Test
    @DisplayName("POST /v1/auth/login - Failure (Invalid Credentials) returns 400")
    void login_ShouldReturn400_WhenCredentialsAreInvalid() throws Exception {
        UserRequestDTO request = UserRequestDTO.builder()
                .email("admin@edu.com").password("wrongpass").role("ADMIN").build();

        when(userFactory.verify(any(UserRequestDTO.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/v1/auth/login")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    @DisplayName("POST /v1/auth/refresh - Failure (Token Expired) returns 400")
    void refresh_ShouldReturn400_WhenTokenIsExpired() throws Exception {
        RefreshToken requestToken = new RefreshToken();
        requestToken.setToken("expired-refresh-token");

        RefreshToken dbToken = new RefreshToken();
        dbToken.setToken("expired-refresh-token");

        when(refreshTokenService.findByToken(any())).thenReturn(Optional.of(dbToken));
        doThrow(new RuntimeException("Refresh token was expired"))
                .when(refreshTokenService).verifyToken(any(RefreshToken.class));

        mockMvc.perform(post("/v1/auth/refresh")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Refresh token was expired"));

    }

    @Test
    @DisplayName("POST /v1/auth/refresh - Success")
    void refresh_ShouldReturnNewTokens_WhenTokenIsValid() throws Exception {
        RefreshToken requestToken = new RefreshToken();
        requestToken.setToken("valid-refresh-token");

        User mockUser = User.builder().email("student@edu.com").build();
        RefreshToken dbToken = new RefreshToken();
        dbToken.setToken("valid-refresh-token");
        dbToken.setUser(mockUser);

        // Mocking the sequence of the refresh logic
        when(refreshTokenService.findByToken(any())).thenReturn(Optional.of(dbToken));
        when(refreshTokenService.verifyToken(any())).thenReturn(dbToken);
        when(jwtService.generateToken("student@edu.com")).thenReturn("new-access-token");

        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("valid-refresh-token"));
    }
}
