package com.educonnect.controller;



import com.educonnect.model.document.DocType;
import com.educonnect.service.contract.StudentDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/doc")
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    ResponseEntity<String> saveDocument(
            @RequestParam UUID studentUuid,
            @RequestParam DocType docType,
            @RequestParam MultipartFile file
            ){

        String msg = studentDocumentService.saveStudentDocument(studentUuid,docType,file);
        return ResponseEntity.ok("File saved successfully, file uri: " + msg) ;

    }
}
