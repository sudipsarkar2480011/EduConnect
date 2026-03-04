package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.dto.DocStreamDTO;
import com.educonnect.model.document.DocTypeEnum;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.service.contract.StudentDocumentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@WebMvcTest(StudentDocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentDocumentService studentDocumentService;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private EduconnectUserDetailsService educonnectUserDetailsService;

    @Test
    @DisplayName("POST /api/v1/doc/upload - Success")
    void saveDocument_shouldReturnOk() throws Exception {
        // Arrange
        UUID studentId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes());

        when(studentDocumentService.saveStudentDocument(eq(studentId), any(), eq(DocTypeEnum.ADHAAR)))
                .thenReturn("Document saved successfully");

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/doc/upload")
                        .file(file)
                        .param("studentUuid", studentId.toString())
                        .param("docType", "ADHAAR"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document saved successfully"));
    }

    @Test
    @DisplayName("GET /api/v1/doc/view/{uuid} - Return PDF")
    void viewImage_shouldStreamPdf() throws Exception {
        // Arrange
        UUID docId = UUID.randomUUID();
        byte[] content = "fake-pdf-data".getBytes();

        StudentDocument docMetadata = new StudentDocument();
        docMetadata.setFileName("my_file.pdf");
        docMetadata.setFileType(FileTypeEnum.PDF);

        DocStreamDTO dto = new DocStreamDTO(new ByteArrayInputStream(content), docMetadata);

        when(studentDocumentService.getDocument(docId)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/doc/view/{documentUuid}", docId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=\"my_file.pdf\""))
                .andExpect(header().string("Cache-Control", "public, max-age=86400"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andExpect(content().bytes(content));
    }
}