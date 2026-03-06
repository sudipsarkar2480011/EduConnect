package com.educonnect.dto.assessment;

import com.educonnect.model.assessment.AssessmentType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                        value = CreateAssignmentRequestDTO.class,
                        name = "ASSIGNMENT"
                ),
                @JsonSubTypes.Type(
                        value = CreateQuizRequestDTO.class,
                        name = "QUIZ"
                )
        }
)
public class CreateAssessmentRequestDTO {

    private Double maxScore;
    private String title;

    private AssessmentType assessmentType;

    private UUID courseId;

    private LocalDate dueDate;


}
