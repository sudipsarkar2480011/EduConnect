package com.educonnect.dto.assessment.create.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionDTO {
    private String questionText;
    @JsonProperty("questionOptions")
    private List<QuestionOptionDTO> questionOptions;
}