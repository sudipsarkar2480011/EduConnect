package com.educonnect.service.contract;

import com.educonnect.model.document.DocType;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StudentDocumentService {

    String saveStudentDocument(UUID studentUuid, DocType docType, MultipartFile file);
}
