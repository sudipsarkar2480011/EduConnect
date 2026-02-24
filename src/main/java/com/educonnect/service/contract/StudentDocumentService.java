package com.educonnect.service.contract;

import com.educonnect.dto.DocStreamDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StudentDocumentService {

    UUID saveStudentDocument(UUID studentUuid,MultipartFile file);

    DocStreamDTO getDocument(UUID documentUuid);
}
