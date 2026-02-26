package com.educonnect.service.contract;

import com.educonnect.dto.DocStreamDTO;
import com.educonnect.model.document.DocTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StudentDocumentService {
    String saveStudentDocument(UUID studentUuid, MultipartFile file, DocTypeEnum docTypeEnum);

    DocStreamDTO getDocument(UUID documentUuid);
}
