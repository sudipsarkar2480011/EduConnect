package com.educonnect.factory.assessment;

import com.educonnect.dto.assessment.CreateAssessmentRequestDTO;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.strategy.assignment.AssessmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssessmentFactory {
    private final List<AssessmentStrategy> assessmentStrategyList;

    public String createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO){

         return assessmentStrategyList.stream()
                .filter(assessmentStrategy -> assessmentStrategy.supports(assessmentRequestDTO.getAssessmentType()))
                .map(assessmentStrategy -> assessmentStrategy.createAssessment(teacher,assessmentRequestDTO))
                .toList().getFirst();

    }
}
