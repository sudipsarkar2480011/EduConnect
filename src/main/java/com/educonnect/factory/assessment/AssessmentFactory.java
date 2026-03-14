package com.educonnect.factory.assessment;

import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.service.strategy.assignment.AssessmentStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Factory method responsible for routing the assessment creation & submission request
 * * <p> It dynamically selects & return the correct {@link AssessmentStrategy}( either Assignment or Quiz) based on the {@link AssessmentType}</p>
 *
 * @see com.educonnect.service.strategy.assignment.AssessmentStrategy
 *
 * @author SudipSarkar
 * @version 1.0
 * @since 1.0
 */


@Component
@RequiredArgsConstructor
public class AssessmentFactory {
    private final List<AssessmentStrategy> assessmentStrategyList;

    /**
     * <p>Routes the student creation request to appropriate strategy </p>
     * @param teacher The Teacher creating the assignment
     * @param assessmentRequestDTO the payload
     * @return A success message
     */
    public Map<String, String> createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO) throws BadRequestException {

        List<Map<String,String>> list = new ArrayList<>();
        for (AssessmentStrategy assessmentStrategy : assessmentStrategyList) {
            if (assessmentStrategy.supports(assessmentRequestDTO.getAssessmentType())) {
                var assessment = assessmentStrategy.createAssessment(teacher, assessmentRequestDTO);
                list.add(assessment);
            }
        }
        return list.getFirst();
    }

    /**
     *
     * @param user The user(student) submitting the assignment
     * @param assessmentRequestDTO the payload
     * @return A success message
     */
    public Map<String,String> submitAssessment(Student user, AssessmentRequestDTO assessmentRequestDTO){

        return assessmentStrategyList.stream()
                .filter(assessmentStrategy -> assessmentStrategy.supports(assessmentRequestDTO.getAssessmentType()))
                .map(assessmentStrategy -> assessmentStrategy.submitAssessment(user,assessmentRequestDTO))
                .toList().getFirst();
    }
}
