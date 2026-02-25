package com.educonnect.dto;

import com.educonnect.model.document.StudentDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@AllArgsConstructor
public class DocStreamDTO {
    private InputStream inputStream;
    private StudentDocument studentDocument;

}
