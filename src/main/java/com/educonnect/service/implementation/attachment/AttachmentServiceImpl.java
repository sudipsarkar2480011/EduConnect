package com.educonnect.service.implementation.attachment;

import com.educonnect.dto.attachment.AttachmentStreamDTO;
import com.educonnect.exception.custom_exceptions.DocumentExceptions;
import com.educonnect.model.document.attachment.Attachment;
import com.educonnect.repo.attachment.AttachmentRepo;
import com.educonnect.service.contract.attachment.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepo attachmentRepo;

    @Override
    public AttachmentStreamDTO getAttachment(UUID attachmentId) throws DocumentExceptions {

        Attachment attachment = attachmentRepo
                .findById(attachmentId)
                .orElseThrow(() -> new DocumentExceptions("Attachment not found"));

        byte[] fileData = attachment.getFileData();

        if (fileData == null) {
            throw new DocumentExceptions("Document has no data");
        }

        InputStream inputStream = new ByteArrayInputStream(fileData) ;

        return new AttachmentStreamDTO(attachment,inputStream);

    }
}
