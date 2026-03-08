package com.educonnect.dto.assessment;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionDTO {
    private String questionText;
    private List<QuestionOptionDTO> questionOptions;
}