package com.educonnect.service.implementation;


import com.educonnect.dto.DocStreamDTO;
import com.educonnect.model.document.DocType;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentDocumentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentDocumentServiceImpl implements StudentDocumentService {

    private final StudentDocumentRepo studentDocumentRepo;
    private final StudentRepo studentRepo;

    @Override
    public UUID saveStudentDocument(UUID studentUuid, MultipartFile file) {

        if(file == null || file.isEmpty()){
            throw new RuntimeException("file not found");
        }

        Student student = studentRepo
                .findByStudentUuid(studentUuid)
                .orElseThrow(()-> new RuntimeException("Student not found"))
        ;

        StudentDocument document = new StudentDocument();

        document.setStudent(student);
        document.setFileName(file.getOriginalFilename());
        document.setDocType(getDocType(file.getOriginalFilename()));

        try {
            document.setFileData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentDocumentRepo.save(document);

        return document.getDocumentUuid();

    }

    private DocType getDocType(String filename){
        filename = filename.toLowerCase();

        if(filename.endsWith(".pdf")){
            return DocType.PDF;
        }
        else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
            return DocType.JPEG;
        }
        else if(filename.endsWith(".png")){
            return  DocType.PNG;
        }
        else {
            return DocType.BYTE_STREAM;
        }
    }

    @Override
    public DocStreamDTO getDocument(UUID documentUuid) {
        StudentDocument document = studentDocumentRepo
                .findByDocumentUuid(documentUuid)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        byte[] fileByteData = document.getFileData();

        if (fileByteData == null) {
            throw new RuntimeException("Document has no data");
        }

        InputStream inputStream = new ByteArrayInputStream(fileByteData);

        return new DocStreamDTO(inputStream,document);
    }



}
