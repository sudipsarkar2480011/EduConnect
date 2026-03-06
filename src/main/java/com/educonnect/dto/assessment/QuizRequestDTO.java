package com.educonnect.dto.assessment;

import com.educonnect.model.assessment.Question;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class QuizRequestDTO extends AssessmentRequestDTO {

    private List<Question> questionList;
}
