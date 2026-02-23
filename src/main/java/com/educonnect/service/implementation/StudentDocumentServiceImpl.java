package com.educonnect.service.implementation;


import com.educonnect.model.document.DocType;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentDocumentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String saveStudentDocument(UUID studentUuid, DocType docType, MultipartFile file) {

        if(file == null || file.isEmpty()){
            throw new RuntimeException("file not found");
        }

        Student student = studentRepo
                .findByStudentUuid(studentUuid)
                .orElseThrow(()-> new RuntimeException("Student not found"))
        ;

        StudentDocument document = new StudentDocument();

        document.setStudent(student);
        document.setDocType(docType);
        document.setFileName(file.getOriginalFilename());

        try {
            document.setFileData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentDocumentRepo.save(document);

        return "Uploaded " + document.getFileName() + " successfully";

    }




}
