package com.educonnect.controller;

import com.educonnect.config.JWTService;
import com.educonnect.dto.LoginRequestDTO;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.factory.UserFactory;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.User;
import com.educonnect.service.contract.auth.AuthService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private com.educonnect.config.EduconnectUserDetailsService educonnectUserDetailsService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserFactory userFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /v1/auth/register - Success")
    void register_ShouldReturnUser_WhenValidRequest() throws Exception {
        User inputUser = User.builder()
                .email("student@edu.com")
                .password("StrongPass123!")
                .role(Role.STUDENT)
                .fullName("John Doe")
                .build();

        when(userFactory.executeSave(any(User.class))).thenReturn(inputUser);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@edu.com"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    @DisplayName("POST /v1/auth/login - Success")
    void login_ShouldReturnResponseDTO_WhenCredentialsAreValid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("admin@edu.com", "adminPass", "ADMIN");
        LoginResponseDTO response = LoginResponseDTO.builder()
                .token("mocked-jwt-token")
                .email("admin@edu.com")
                .role("ADMIN")
                .name("Admin User")
                .build();

        when(userFactory.verify(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }
}