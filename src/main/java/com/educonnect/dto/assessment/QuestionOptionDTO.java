package com.educonnect.dto.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionOptionDTO{
    private String optionText;
    @JsonProperty("isCorrectOption")
    private Boolean isCorrectOption ;
}
