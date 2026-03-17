package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Admin;
import com.educonnect.service.contract.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @TestConfiguration
    static class TestJacksonConfig {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private EduconnectUserDetailsService educonnectUserDetailsService;

    private Admin mockAdmin;
    private UUID adminId;
    private static final String BASE_URL = "/v1/api/admin";

    @BeforeEach
    void setUp() {
        adminId = UUID.randomUUID();
        mockAdmin = new Admin();
        mockAdmin.setFullName("John Admin");
        mockAdmin.setEmail("john@educonnect.com");
    }

    @Test
    @DisplayName("GET /admin/{id} - Success")
    void getById_Success() throws Exception {
        when(adminService.getById(adminId)).thenReturn(mockAdmin);

        mockMvc.perform(get(BASE_URL + "/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Admin"))
                .andExpect(jsonPath("$.email").value("john@educonnect.com"));
    }

    @Test
    @DisplayName("GET /admin/{id} - User Not Found")
    void getById_NotFound() throws Exception {
        when(adminService.getById(adminId)).thenThrow(new UserNotFoundException("User Not Found"));

        mockMvc.perform(get(BASE_URL + "/{id}", adminId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /admin/{id} - Success")
    void updateAdmin_Success() throws Exception {
        when(adminService.update(eq(adminId), any(Admin.class))).thenReturn(mockAdmin);

        mockMvc.perform(put(BASE_URL + "/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdmin)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User updated successfully")));
    }

    @Test
    @DisplayName("PUT /admin/{id} - Failure (User missing)")
    void updateAdmin_NotFound() throws Exception {
        when(adminService.update(eq(adminId), any(Admin.class)))
                .thenThrow(new UserNotFoundException("User don't exists"));

        mockMvc.perform(put(BASE_URL + "/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdmin)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /admin/{id} - Success")
    void deleteAdmin_Success() throws Exception {
        doNothing().when(adminService).delete(adminId);

        mockMvc.perform(delete(BASE_URL + "/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User deleted Successfully")));

        verify(adminService, times(1)).delete(adminId);
    }

    @Test
    @DisplayName("DELETE /admin/{id} - Failure (Already Deleted or Missing)")
    void deleteAdmin_NotFound() throws Exception {
        doThrow(new UserNotFoundException("User Not Found")).when(adminService).delete(adminId);

        mockMvc.perform(delete(BASE_URL + "/{id}", adminId))
                .andExpect(status().isNotFound());
    }
}