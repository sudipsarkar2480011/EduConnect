package com.educonnect.dto.assessment.create.quiz;

import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class CreateQuizRequestDTO extends CreateAssessmentRequestDTO {

    List<QuizQuestionDTO> questionDTOList;


}
