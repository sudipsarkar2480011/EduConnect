package com.educonnect.service.contract.quiz;

import com.educonnect.dto.assessment.report.quiz.StudentQuizReportDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.model.user.User;
import org.apache.coyote.BadRequestException;

import java.util.UUID;

public interface QuizService {
    QuizServeDTO getQuiz(UUID assessmentId);
    StudentQuizReportDTO getQuizReport(UUID submissionId, User user) throws BadRequestException;
}
