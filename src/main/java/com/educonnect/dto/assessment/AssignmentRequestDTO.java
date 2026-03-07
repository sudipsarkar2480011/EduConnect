package com.educonnect.dto.assessment;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class AssignmentRequestDTO extends AssessmentRequestDTO{
    private UUID assignment_id;
    private List<MultipartFile> files;
}
