package com.educonnect.dto.assessment;

import com.educonnect.model.assessment.Question;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@ToString(callSuper = true)
@Data
public class QuizRequestDTO extends AssessmentRequestDTO {

    private List<Question> questionList;
}
