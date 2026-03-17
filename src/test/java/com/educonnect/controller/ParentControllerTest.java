package com.educonnect.controller;

import com.educonnect.EdupointBackendApplication;
import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.config.JwtFilter;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.service.contract.parent.ParentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ParentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ParentService parentService;
//    @MockitoBean
//    private JWTService jwtService;
//    @MockitoBean
//    private EduconnectUserDetailsService educonnectUserDetailsService;
//    @MockitoBean
//    private JwtFilter jwtFilter;
    @Test
    void testGetParentById() throws Exception{
        UUID parentId=UUID.randomUUID();
        ParentResponseDTO dto=new ParentResponseDTO();
        dto.setId(parentId);
        dto.setName("Maha");
        Mockito.when(parentService.getById(parentId)).thenReturn(dto);
        mockMvc.perform(get("/v1/api/parent/"+parentId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
    @Test
    void testDeleteParent() throws Exception{
        UUID parentId=UUID.randomUUID();
        Mockito.doNothing().when(parentService).delete(parentId);
        mockMvc.perform(delete("/v1/api/parent/"+parentId)).andExpect(status().isNoContent());
    }
}