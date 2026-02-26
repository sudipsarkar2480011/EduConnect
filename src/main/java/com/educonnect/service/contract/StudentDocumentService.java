package com.educonnect.service.contract;

import com.educonnect.dto.DocStreamDTO;
import com.educonnect.model.document.DocTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Service for managing student document
 * @author sudipsarkar
 * @version 1.0
 * @since 1.0
 *
 */
public interface StudentDocumentService {

    /**
     *
     * save the student document in the DB (BLOB)
     * @param studentUuid The unique identifier to find the student whose document will be uploaded
     * @param file The document file
     * @param docTypeEnum The type of the document(ADHAAR, PAN...)
     * @return The document URI
     * @since 1.0
     *
     */
    String saveStudentDocument(UUID studentUuid, MultipartFile file, DocTypeEnum docTypeEnum);


    /**
     *
     * fetch the student document data from the DB (BLOB)
     * @param documentUuid To find the document
     * @return The {@link DocStreamDTO} object containing the file data.
     * @since 1.0
     */
    DocStreamDTO getDocument(UUID documentUuid);
}
