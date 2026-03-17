package com.educonnect.controller;

import com.educonnect.dto.parent.ParentAccessResponseDTO;
import com.educonnect.service.contract.parent.ParentAccessService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ParentAccessControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ParentAccessService parentAccessService;
    @Test
    void testGrantAccess() throws Exception{
        ParentAccessResponseDTO response=new ParentAccessResponseDTO();
        response.setPermissions("VIEW_ATTENDANCE");
        Mockito.when(parentAccessService.grantAccess(Mockito.any())).thenReturn(response);
        String requestBody= """
                {
                "parentId":"11111111-1111-1111-1111-111111111111",

                "studentId":"22222222-2222-2222-2222-222222222222",

                "permissions":"VIEW_ATTENDANCE",

                "status":"ACTIVE"

                        }""";
        mockMvc.perform(post("/v1/api/parent/access").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());

    }
    @Test
    void testGetAccess() throws Exception{
        UUID parentId=UUID.randomUUID();
        UUID studentId=UUID.randomUUID();
        ParentAccessResponseDTO response=new ParentAccessResponseDTO();
        response.setPermissions("VIEW_RESULTS");
        Mockito.when(parentAccessService.getAccess(parentId,studentId)).thenReturn(response);
        mockMvc.perform(get("/v1/api/parent/access").param("parentId",parentId.toString()).param("studentId",studentId.toString())).andExpect(status().isOk());

    }
}