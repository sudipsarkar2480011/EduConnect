package com.educonnect.dto.assessment.submit.quiz;

import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.model.assessment.Question;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Data
public class QuizRequestDTO extends AssessmentRequestDTO {

    private List<Question> questionList;
}
