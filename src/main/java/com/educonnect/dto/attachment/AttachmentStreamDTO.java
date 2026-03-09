package com.educonnect.dto.attachment;

import com.educonnect.model.document.attachment.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@AllArgsConstructor
@Getter
public class AttachmentStreamDTO {
    private Attachment attachment;
    private InputStream inputStream;
}

