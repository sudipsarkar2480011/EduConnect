package com.educonnect.service.strategy.assessment;

import com.educonnect.dto.assessment.report.AssessmentReportDTO;
import com.educonnect.dto.assessment.serve.AssessmentServeDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import org.apache.coyote.BadRequestException;

import java.util.Map;
import java.util.UUID;

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
    Map<String,String> submitAssessment(Student student , AssessmentRequestDTO assessmentRequestDTO) throws BadRequestException;

    /**
     * Handles assessment creation
     * @param teacher The teacher creating the assessment
     * @param assessmentRequestDTO The payload
     * @return A success message (Might change in future)
     */
    Map<String,String> createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO) throws BadRequestException;


    default boolean canCreateAssessment(Teacher teacher, Course course){
        return teacher.getUserId().equals(course.getTeacher().getUserId());
    }

    AssessmentServeDTO serveAssessment(UUID assessmentId,User user) throws BadRequestException;

    AssessmentReportDTO getReport(UUID submissionId, User user) throws BadRequestException;

}
