package com.educonnect.dto.assessment.serve.quiz;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionOptionDTO {
    private String optionText;
    private UUID questionOptionId;
}
