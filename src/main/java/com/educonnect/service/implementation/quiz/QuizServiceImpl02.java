package com.educonnect.service.implementation.quiz;

import com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.Quiz;
import com.educonnect.repo.assessment.quiz.QuizRepo;
import com.educonnect.service.contract.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class QuizServiceImpl02 implements QuizService {

    private final QuizRepo quizRepo;

    @Override
    public QuizServeDTO getQuiz(UUID assessmentId) {

        Quiz quiz = quizRepo.findQuizWithQuestionAndOptions(assessmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));
        return mapToQuizServeDTO(quiz);
    }

    public QuizServeDTO mapToQuizServeDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }
        List<QuizQuestionDTO> questionDTOs = quiz.getQuestionList().stream()
                .map(question -> {
                    QuizQuestionDTO qDto = new QuizQuestionDTO();
                    qDto.setQuizQuestionId(question.getQuestionId());
                    qDto.setQuestionText(question.getQuestionText());

                    // 2. Map Options for each Question
                    if (question.getQuestionOptionList() != null) {
                        List<QuestionOptionDTO> optionDTOs = question.getQuestionOptionList().stream()
                                .map(option -> {
                                    QuestionOptionDTO oDto = new QuestionOptionDTO();
                                    oDto.setQuestionOptionId(option.getQuestionOptionId());
                                    oDto.setOptionText(option.getOptionText());
                                    return oDto;
                                })
                                .toList();
                        qDto.setQuestionOptionDTOList(optionDTOs);
                    }
                    return qDto;
                })
                .toList();
        return new QuizServeDTO(quiz.getQuizId(), questionDTOs);
    }
}
