package com.educonnect.service.implementation;


import com.educonnect.dto.DocStreamDTO;
import com.educonnect.model.document.DocType;
import com.educonnect.model.document.DocTypeEnum;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.user.Student;
import com.educonnect.repo.DocTypeRepo;
import com.educonnect.repo.StudentDocumentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class StudentDocumentServiceImpl implements StudentDocumentService {

    private final StudentDocumentRepo studentDocumentRepo;
    private final StudentRepo studentRepo;
    private final DocTypeRepo docTypeRepo;




    @Override
    public UUID saveStudentDocument(UUID studentUuid, MultipartFile file, DocTypeEnum docTypeEnum) {

        if(file == null || file.isEmpty()){
            throw new RuntimeException("file not found");
        }

        Student student = studentRepo
                .findByUserId(studentUuid)
                .orElseThrow(()-> new RuntimeException("Student not found"))
        ;

        StudentDocument document = new StudentDocument();

        document.setStudent(student);
        document.setFileName(file.getOriginalFilename());
        FileTypeEnum fileType = getFileType(file.getOriginalFilename());

        DocType docType = null;

        docType = docTypeRepo.findByDocTypeName(docTypeEnum).orElse(null);

        if(docType == null){
            docType = DocType.builder()
                    .docTypeName(docTypeEnum)
                    .description("LATER.....")
                    .build();

            docTypeRepo.save(docType);
        }

        document.setDocType(docType);
        document.setFileType(fileType);

        try {
            document.setFileData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        studentDocumentRepo.save(document);

        return document.getStudentDocumentId();

    }

    private FileTypeEnum getFileType(String filename){
        filename = filename.toLowerCase();

        if(filename.endsWith(".pdf")){
            return FileTypeEnum.PDF;
        }
        else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
            return FileTypeEnum.JPEG;
        }
        else if(filename.endsWith(".png")){
            return  FileTypeEnum.PNG;
        }
        else {
            return FileTypeEnum.BYTE_STREAM;
        }
    }

    @Override
    public DocStreamDTO getDocument(UUID documentUuid) {
        StudentDocument document = studentDocumentRepo
                .findByStudentDocumentId(documentUuid)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        byte[] fileByteData = document.getFileData();

        if (fileByteData == null) {
            throw new RuntimeException("Document has no data");
        }

        InputStream inputStream = new ByteArrayInputStream(fileByteData);

        return new DocStreamDTO(inputStream,document);
    }

}
