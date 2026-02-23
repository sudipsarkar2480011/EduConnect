package com.educonnect.controller;



import com.educonnect.model.document.DocType;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/doc")
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    ResponseEntity<String> saveDocument(
            @RequestParam String studentUuid,
            @RequestParam DocType docType,
            @RequestParam MultipartFile file
            ){

        String msg = studentDocumentService.saveStudentDocument(UUID.fromString(studentUuid),docType,file);
        return ResponseEntity.ok("File saved successfully, file uri: " + msg) ;
    }

    @GetMapping(path = "view/{image}")
    public void viewImage(@PathVariable(name = "image") String image, HttpServletResponse response) throws IOException {
         InputStream imageStream = studentDocumentService.getResource(image);
         response.setContentType(MediaType.IMAGE_JPEG_VALUE);
         response.setHeader("Content-Disposition", "inline; filename=\"" + image + "\"");
         response.setHeader("Cache-Control", "public, max-age=86400"); // optional

        StreamUtils.copy(imageStream,response.getOutputStream());
    }
}
