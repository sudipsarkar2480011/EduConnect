package com.educonnect.service.implementation;


import com.educonnect.dto.doctype.DocStreamDTO;
import com.educonnect.model.document.DocType;
import com.educonnect.model.document.DocTypeEnum;
import com.educonnect.model.document.FileTypeEnum;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.user.Admin;
import com.educonnect.model.user.Role;
import com.educonnect.model.user.Student;
import com.educonnect.repo.AdminRepo;
import com.educonnect.repo.DocTypeRepo;
import com.educonnect.repo.StudentDocumentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.ParentService;
import com.educonnect.service.contract.StudentDocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentDocumentServiceImpl implements StudentDocumentService {

    private final StudentDocumentRepo studentDocumentRepo;
    private final StudentRepo studentRepo;
    private final DocTypeRepo docTypeRepo;

    private final Map<String,FileTypeEnum> allowedTypes =
            new HashMap<>(Map.of(
                    ".pdf",FileTypeEnum.PDF,
                    "jpeg",FileTypeEnum.JPEG,
                    "jpg",FileTypeEnum.JPEG
            ));


    @Override
    public String saveStudentDocument(UUID studentUuid, MultipartFile file, DocTypeEnum docTypeEnum) {

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
        DocType docType = docTypeRepo.findByDocTypeName(docTypeEnum).orElse(null);

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
        document.setStudentDocumentId(UUID.randomUUID());
        String uri =  ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/api/doc/view/")
                .path(document.getStudentDocumentId().toString())
                .toUriString();
        document.setFileUri(uri);
        studentDocumentRepo.save(document);
        return uri;

    }

    private FileTypeEnum getFileType(String filename){
        filename = filename.toLowerCase();
        String extension= filename.substring(filename.lastIndexOf("."));
        return allowedTypes.getOrDefault(extension,FileTypeEnum.BYTE_STREAM);

//        if(filename.endsWith(".pdf")){
//            return FileTypeEnum.PDF;
//        }
//        else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
//            return FileTypeEnum.JPEG;
//        }
//        else if(filename.endsWith(".png")){
//            return  FileTypeEnum.PNG;
//        }
//        else {
//            return FileTypeEnum.BYTE_STREAM;
//        }
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


    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class AdminServiceImpl implements ParentService.AdminService {

        private final AdminRepo adminRepo;
        private final BCryptPasswordEncoder encoder;

        @Override
        public Admin create(Admin incoming) {
            Admin toSave = Admin.builder()
                    .fullName(incoming.getFullName())
                    .email(incoming.getEmail())
                    .password(encoder.encode(incoming.getPassword()))
                    .role(Role.ADMIN)
                    .build();

            return adminRepo.save(toSave);
        }

        @Override
        @Transactional(readOnly = true)
        public Admin getById(UUID id) {
            return adminRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Admin not found with id: " + id));
        }

        @Override
        @Transactional(readOnly = true)
        public List<Admin> getAll() {
            return adminRepo.findAll();
        }

        @Override
        public Admin update(UUID id, Admin incoming) {
            Admin existing = getById(id);

            // update allowed fields (avoid null overwrites unless provided)
            if (incoming.getFullName() != null) existing.setFullName(incoming.getFullName());
            if (incoming.getEmail() != null) existing.setEmail(incoming.getEmail());

            // keep role as ADMIN
            existing.setRole(Role.ADMIN);

            // If password provided in update, re-encode it
            if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
                existing.setPassword(encoder.encode(incoming.getPassword()));
            }

            return adminRepo.save(existing);
        }

        @Override
        public void changePassword(UUID id, String newRawPassword) {
            Admin admin = getById(id);
            admin.setPassword(encoder.encode(newRawPassword));
            adminRepo.save(admin);
        }

        @Override
        public void delete(UUID id) {
            if (!adminRepo.existsById(id)) {
                throw new EntityNotFoundException("Admin not found with id: " + id);
            }
            adminRepo.deleteById(id);
        }
    }
}
