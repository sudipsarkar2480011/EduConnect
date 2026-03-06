package com.educonnect.dto.assessment;

import com.educonnect.model.assessment.Assessment;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;


@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                    value = AssignmentRequestDTO.class,
                    name = "ASSIGNMENT"
                ),
                @JsonSubTypes.Type(
                        value = QuizRequestDTO.class,
                        name = "QUIZ"
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AssessmentRequestDTO {

    private String description;
    private LocalDate dueDate;
    private UUID assessment_id;
}
