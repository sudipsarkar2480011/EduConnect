package com.educonnect.dto.assessment.serve.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class QuizQuestionServeDTO {
    private UUID quizQuestionId;
    private String questionText;
    @JsonProperty("questionOptions")
    private List<QuestionOptionServeDTO> questionOptionServeDTOList;
}
