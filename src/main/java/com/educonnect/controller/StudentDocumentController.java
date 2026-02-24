package com.educonnect.controller;



import com.educonnect.model.document.DocType;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/doc")
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    ResponseEntity<String> saveDocument(
            @RequestParam String studentUuid,
            @RequestParam MultipartFile file
            ){
        try{
            UUID documentUuid = studentDocumentService.saveStudentDocument(UUID.fromString(studentUuid),file);
            return ResponseEntity.ok(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("api/doc/view")
                            .path(documentUuid.toString())
                            .toUriString()
            ) ;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "view/{documentUuid}")
    public void viewImage(
            @PathVariable("documentUuid") UUID documentUuid,
            HttpServletResponse response
    ) throws IOException {
        var fileData = studentDocumentService.getDocument(documentUuid);

        var document = fileData.getStudentDocument();
        var inputStream = fileData.getInputStream();

        response.setHeader("Content-Disposition", "inline; filename=\"" + document.getFileName() + "\"");
        response.setHeader("Cache-Control", "public, max-age=86400"); // optional

        if(document.getDocType() == DocType.PDF){
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        }
        else if (document.getDocType() == DocType.JPEG){
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        }
        else if (document.getDocType() == DocType.PNG) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }else{
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        }

        StreamUtils.copy(inputStream,response.getOutputStream());
    }
}
