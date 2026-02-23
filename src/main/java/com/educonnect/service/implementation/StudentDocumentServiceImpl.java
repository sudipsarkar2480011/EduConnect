package com.educonnect.service.implementation;


import com.educonnect.model.document.DocType;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.user.Student;
import com.educonnect.repo.StudentDocumentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @Value("${storage.upload-dir:uploads}")
    private String uploadDir;
    
    private Path uploadPath ;

    @Value("${server.port}")
    private String port ;

    @PostConstruct
    public void init(){
        this.uploadPath =  Paths.get(uploadDir)
                                .toAbsolutePath()
                                .normalize();
        try {
            Files.createDirectories(uploadPath);
        }catch (IOException e){
            throw new RuntimeException("Could not create upload directory");
        }
    }





    @Override
    public String saveStudentDocument(UUID studentUuid, DocType docType, MultipartFile file) {
        Student student = studentRepo
                .findByStudentUuid(studentUuid)
                .orElseThrow(()-> new RuntimeException("Student not found"))
        ;



        String originalFileName = file.getOriginalFilename();
        int indexOfDot = originalFileName.lastIndexOf(".");
        String extension =
                originalFileName.substring(indexOfDot);


        String newFileName = originalFileName.substring(0,indexOfDot)
                            + UUID.randomUUID()
                            + extension;

        Path targetLocation = uploadPath.resolve(newFileName);

        try {
            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        
       
        studentDocumentRepo.save(StudentDocument.builder()
        .student(student)
        .docType(docType)
        .FileURI(newFileName)
        .build());

        return newFileName;

    }


    @Override
    public InputStream getResource(String fileName) throws FileNotFoundException {
       String fullPath =uploadPath+File.separator+fileName;
       InputStream file = new FileInputStream(fullPath);
       return file;
    }

    @Override
    public Path getUploadPath() {
        return uploadPath;
    }


}
