package com.educonnect.service.strategy.result.impl;

import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.service.strategy.result.ResultStrategy;

import java.util.UUID;

public class QuizResultStrategy implements ResultStrategy {
    @Override
    public boolean supports(AssessmentType assessmentType) {
        return assessmentType.toString().equals("QUIZ")
                || assessmentType.toString().equals("QUIZ_SUBMISSION");
    }

    @Override
    public void computeResult(UUID assessmentId, AssessmentType assessmentType) {

    }
}
