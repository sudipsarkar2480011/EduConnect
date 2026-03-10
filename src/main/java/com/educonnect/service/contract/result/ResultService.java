package com.educonnect.service.contract.result;

import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.user.Teacher;

import java.util.UUID;

/**
 * Contract for {@link Result} related operations
 */
public interface ResultService {
    /**
     * This method computes the result of an attempted {@link com.educonnect.model.assessment.Quiz}
     * (the teacher does not have to evaluate , the score of the Quiz shall be computed automatically)
     * @param assessmentId The unique identifier of the assessment(Quiz)
     * @param studentId The unique identifier of student who submitted the assessment (attempted the quiz)
     * @return A success message
     */
    String computeQuizResult(UUID assessmentId,UUID studentId);

    /**
     * This method enables teacher to evaluate an {@link com.educonnect.model.assessment.Assessment} of a student
     * (the teacher has to manually evaluate each assignment)
     * @param assessmentId The unique identifier of the assessment(Assignment)
     * @param studentId The unique identifier of student who submitted the assessment (Assignment)
     * @param teacher The Teacher who evaluates the assignment
     * @param givenScore The score given by the teacher
     * @return A success message
     */
    String evaluateStudent(UUID assessmentId, UUID studentId, Teacher teacher, double givenScore);

    /**
     * This method returns the result
     * @param resultId The unique identifier of the Result
     * @return The {@link Result}
     */
    Result getResultWithId(UUID resultId);
}
