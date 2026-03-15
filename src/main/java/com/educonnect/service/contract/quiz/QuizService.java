package com.educonnect.service.contract.quiz;

import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;

import java.util.UUID;

public interface QuizService {
    QuizServeDTO getQuiz(UUID assessmentId);
}
