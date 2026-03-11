package com.educonnect.service.strategy.assignment;

import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;

/**
 * Defines the contract for assessment operations
 * @author SudipSarkar
 * @version 1.0
 * @since 1.0
 */
public interface AssessmentStrategy {

    /**
     * Check if the strategy implementation supports the given assessment type
     * @param type The type of the assessment ({@link AssessmentType})
     * @return true if supported , false if not
     */
    boolean supports(AssessmentType type);

    /**
     * Handles assessment submission
     * @param student The student submitting the assessment
     * @param assessmentRequestDTO The payload
     * @return A success message (Might change in future)
     */
    String submitAssessment(Student student , AssessmentRequestDTO assessmentRequestDTO);

    /**
     * Handles assessment creation
     * @param teacher The teacher creating the assessment
     * @param assessmentRequestDTO The payload
     * @return A success message (Might change in future)
     */
    String createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO);
}
