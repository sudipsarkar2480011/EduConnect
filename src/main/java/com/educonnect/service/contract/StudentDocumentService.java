package com.educonnect.service.contract;

import com.educonnect.model.document.DocType;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

public interface StudentDocumentService {

    String saveStudentDocument(UUID studentUuid, DocType docType, MultipartFile file);
    InputStream getResource(String fileName) throws FileNotFoundException;
    public Path getUploadPath();
}
