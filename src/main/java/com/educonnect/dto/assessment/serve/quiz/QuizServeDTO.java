package com.educonnect.dto.assessment.serve.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ToString
@Data
public class QuizServeDTO {
    private UUID quizId;
    List<QuizQuestionDTO> quizQuestionDTOList;
}
