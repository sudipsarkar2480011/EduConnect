package com.educonnect.service.contract.result;

import com.educonnect.model.assessment.AssessmentType;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.user.Teacher;

import java.util.UUID;

public interface ResultService {
    String computeQuizResult(UUID assessmentId,UUID studentId);
    String evaluateStudent(UUID assessmentId, UUID studentId, Teacher teacher, double givenScore);
    Result getResultWithId(UUID resultId);
}
