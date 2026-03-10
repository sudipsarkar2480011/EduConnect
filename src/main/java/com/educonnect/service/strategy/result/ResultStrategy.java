package com.educonnect.service.strategy.result;

import com.educonnect.model.assessment.AssessmentType;

import java.util.UUID;

public interface ResultStrategy {
    boolean supports(AssessmentType assessmentType);
    void computeResult(UUID assessmentId, AssessmentType assessmentType);
}
