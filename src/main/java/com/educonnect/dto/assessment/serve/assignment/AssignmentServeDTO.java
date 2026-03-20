package com.educonnect.dto.assessment.serve.assignment;

import com.educonnect.dto.assessment.serve.AssessmentServeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AssignmentServeDTO extends AssessmentServeDTO {
    private String instruction;
    private Integer noOfDocumentsToBeUploaded;

}
