package com.educonnect.dto.assessment;

import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.assessment.AssessmentType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;


@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,property = "assessmentType", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                    value = AssignmentRequestDTO.class,
                    name = "ASSIGNMENT"
                ),
                @JsonSubTypes.Type(
                        value = QuizRequestDTO.class,
                        name = "QUIZ"
                ),

                @JsonSubTypes.Type(
                        value = StudentQuizQuestionResponseDTO.class,
                        name = "QUIZ_SUBMISSION"
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class AssessmentRequestDTO {

    private String description;
    private LocalDate dueDate;
    private UUID assessmentId;
    private AssessmentType assessmentType;
}
