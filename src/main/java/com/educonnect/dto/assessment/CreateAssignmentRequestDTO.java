package com.educonnect.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateAssignmentRequestDTO extends CreateAssessmentRequestDTO{
    private Integer noOfDocumentsToBeUploaded;
}
