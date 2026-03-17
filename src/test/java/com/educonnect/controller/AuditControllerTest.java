package com.educonnect.controller;

import com.educonnect.config.JWTService;
import com.educonnect.config.JwtFilter;
import com.educonnect.model.audit.Action;
import com.educonnect.model.audit.AuditLog;
import com.educonnect.service.contract.audit.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuditControllerTest {

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
    private AuditLogService auditLogService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private UUID userId;
    private AuditLog sampleLog;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        sampleLog = AuditLog.builder()
                .auditLogId(UUID.randomUUID())
                .action(Action.CREATE)
                .resource("COURSE_101")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testViewAuditsByUserId() throws Exception {
        // Arrange
        List<AuditLog> logs = Arrays.asList(sampleLog);
        Mockito.when(auditLogService.findAuditLogByUserId(userId)).thenReturn(logs);

        // Act & Assert
        mockMvc.perform(get("/v1/api/audits/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].resource").value("COURSE_101"))
                .andExpect(jsonPath("$[0].action").value("CREATE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testViewAuditsWithPagination() throws Exception {
        // Arrange
        List<AuditLog> logs = Arrays.asList(sampleLog);
        Mockito.when(auditLogService.findAuditLogByUserId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(logs);

        // Act & Assert
        mockMvc.perform(get("/v1/api/audits/{userId}/from", userId)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testViewAuditsByResource() throws Exception {
        // Arrange
        List<AuditLog> logs = Arrays.asList(sampleLog);
        Mockito.when(auditLogService.findAuditByResource(eq("COURSE_101"), anyInt(), anyInt()))
                .thenReturn(logs);

        // Act & Assert
        mockMvc.perform(get("/v1/api/audits/by")
                        .param("resource", "COURSE_101")
                        .param("from", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resource").value("COURSE_101"));
    }


}