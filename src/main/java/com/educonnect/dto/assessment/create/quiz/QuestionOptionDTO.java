package com.educonnect.dto.assessment.create.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionOptionDTO{
    private String optionText;
    @JsonProperty("isCorrectOption")
    private Boolean isCorrectOption ;
}
