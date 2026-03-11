package com.educonnect.controller;

import com.educonnect.config.EduconnectUserDetailsService;
import com.educonnect.config.JWTService;
import com.educonnect.dto.doctype.DocStreamDTO;
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

    private final String BASE_URL = "/v1/api/doc";

    // --- UPLOAD TESTS ---

    @Test
    @DisplayName("POST /upload - Success")
    void saveDocument_Success() throws Exception {
        UUID studentId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes());

        String expectedUri = "http://localhost/v1/api/doc/view/" + UUID.randomUUID();

        when(studentDocumentService.saveStudentDocument(eq(studentId), any(), eq(DocTypeEnum.ADHAAR)))
                .thenReturn(expectedUri);

        mockMvc.perform(multipart(BASE_URL + "/upload")
                        .file(file)
                        .param("studentUuid", studentId.toString())
                        .param("docType", "ADHAAR"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUri));
    }

    @Test
    @DisplayName("POST /upload - Failure (Invalid UUID)")
    void saveDocument_InvalidUuid_ReturnsInternalError() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());

        // Note: Controller catch (Exception e) throws RuntimeException, which results in 500
        mockMvc.perform(multipart(BASE_URL + "/upload")
                        .file(file)
                        .param("studentUuid", "not-a-uuid")
                        .param("docType", "PAN"))
                .andExpect(status().isBadRequest());
    }

    // --- VIEW/DOWNLOAD TESTS ---

    @Test
    @DisplayName("GET /view/{uuid} - Return PDF")
    void viewImage_PdfSuccess() throws Exception {
        UUID docId = UUID.randomUUID();
        byte[] content = "fake-pdf-data".getBytes();

        StudentDocument docMetadata = new StudentDocument();
        docMetadata.setFileName("my_file.pdf");
        docMetadata.setFileType(FileTypeEnum.PDF);

        DocStreamDTO dto = new DocStreamDTO(new ByteArrayInputStream(content), docMetadata);

        when(studentDocumentService.getDocument(docId)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/view/{documentUuid}", docId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=\"my_file.pdf\""))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE))
                .andExpect(content().bytes(content));
    }

    @Test
    @DisplayName("GET /view/{uuid} - Return JPEG")
    void viewImage_JpegSuccess() throws Exception {
        UUID docId = UUID.randomUUID();
        byte[] content = new byte[]{ (byte)0xFF, (byte)0xD8 }; // JPEG Header

        StudentDocument docMetadata = new StudentDocument();
        docMetadata.setFileName("photo.jpg");
        docMetadata.setFileType(FileTypeEnum.JPEG);

        DocStreamDTO dto = new DocStreamDTO(new ByteArrayInputStream(content), docMetadata);

        when(studentDocumentService.getDocument(docId)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/view/{documentUuid}", docId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(content));
    }

    @Test
    @DisplayName("GET /view/{uuid} - Document Not Found")
    void viewImage_NotFound_ThrowsException() throws Exception {
        UUID docId = UUID.randomUUID();

        when(studentDocumentService.getDocument(docId))
                .thenThrow(new RuntimeException("Document not found"));

        // By default, an unhandled RuntimeException in a test results in a 500 status
        mockMvc.perform(get(BASE_URL + "/view/{documentUuid}", docId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Document not found"));
    }
}