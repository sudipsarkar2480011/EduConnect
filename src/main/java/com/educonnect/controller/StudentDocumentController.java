package com.educonnect.controller;



import com.educonnect.config.UserPrinciples;
import com.educonnect.model.document.DocTypeEnum;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;



/**
 * REST controller for managing student documents.
 * *
 *  @author sudipsarkar
 *  @version 1.0
 *  @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/doc")

public class StudentDocumentController {
    private final StudentDocumentService studentDocumentService;


    /**
     * Uploads and saves a student document to the database.
     * <p>
     * This method accepts a file and stores it as a BLOB in the DB.
     * </p>
     *
     * @param userPrinciple The student whose document will be uploaded.
     * @param file The document file
     * @param docType The type of the document(ADHAAR, PAN...)
     * @since 1.0
     */

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    ResponseEntity<String> saveDocument(
            @RequestParam MultipartFile file,
            @RequestParam DocTypeEnum docType,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ){
        try{
            return ResponseEntity.ok(
                    studentDocumentService.saveStudentDocument(userPrinciple.getUser().getUserId(), file,docType)
            ) ;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     *
     * <p>
     * This method accepts a file and stores it as a BLOB in the DB.
     * </p>
     *
     * @param documentUuid The unique identifier to fetch the document (BLOB).
     * @param response The {@link HttpServletResponse} used to set the Content-Type and stream the file data.
     * @since 1.0
     *
     */
    @GetMapping(path = "view/{documentUuid}")
    public void viewImage(
            @PathVariable("documentUuid") UUID documentUuid,
            HttpServletResponse response
    ) throws IOException {
        var fileData = studentDocumentService.getDocument(documentUuid);

        var document = fileData.getStudentDocument();
        var inputStream = fileData.getInputStream();

        response.setHeader("Content-Disposition",
                "inline; filename=\""  + document.getFileName() + "\"");
        response.setHeader("Cache-Control", "public, max-age=86400"); // optional

        String contentType = MediaTypeFactory.getMediaType(document.getFileName())
                .map(MediaType::toString)
                .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        response.setContentType(contentType);

        StreamUtils.copy(inputStream,response.getOutputStream());
    }
}
