package com.educonnect.dto.assessment.create.assignment;

import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateAssignmentRequestDTO extends CreateAssessmentRequestDTO {
    private Integer noOfDocumentsToBeUploaded;
    private String instruction;
}
