package com.educonnect.service.contract.attachment;

import com.educonnect.dto.attachment.AttachmentStreamDTO;
import com.educonnect.exception.custom_exceptions.DocumentExceptions;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AttachmentService {
    AttachmentStreamDTO getAttachment(UUID attachmentId) throws DocumentExceptions;
}
