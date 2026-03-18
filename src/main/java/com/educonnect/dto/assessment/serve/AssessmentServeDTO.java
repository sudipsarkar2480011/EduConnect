package com.educonnect.dto.assessment.serve;

import com.educonnect.dto.assessment.serve.assignment.AssignmentServeDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.dto.assessment.submit.assignment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.submit.quiz.StudentQuizQuestionResponseDTO;
import com.educonnect.model.assessment.AssessmentType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;


@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,property = "assessmentType", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                        value = AssignmentServeDTO.class,
                        name = "ASSIGNMENT"
                ),
                @JsonSubTypes.Type(
                        value = QuizServeDTO.class,
                        name = "QUIZ"
                )
        }
)
@Data
public class AssessmentServeDTO {
    private String title;
    private AssessmentType assessmentType;
}
