package com.educonnect.service.strategy.assignment;

import com.educonnect.dto.assessment.AssessmentRequestDTO;
import com.educonnect.dto.assessment.CreateAssessmentRequestDTO;
import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;

public interface AssessmentStrategy {

    boolean supports(AssessmentType type);

    String submitAssessment(User user ,AssessmentRequestDTO assessmentRequestDTO);

    String createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO);
}
