package com.educonnect.service.implementation.quiz;

import com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.assessment.Question;
import com.educonnect.model.assessment.QuestionOption;
import com.educonnect.model.assessment.Quiz;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.quiz.QuestionOptionRepo;
import com.educonnect.repo.assessment.quiz.QuestionRepo;
import com.educonnect.repo.assessment.quiz.QuizRepo;
import com.educonnect.service.contract.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepo quizRepo;
    private final QuestionRepo questionRepo;
    private final QuestionOptionRepo questionOptionRepo;
    private final AssessmentRepo assessmentRepo;

    @Override
    public QuizServeDTO getQuiz(UUID assessmentId) {

        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Assessment not found"));

        Quiz quiz = quizRepo.findByAssessmentAssessmentId(assessment.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        //System.out.println("Quiz - " + quiz);

        List<Question> questionList = questionRepo.findAllByQuizQuizId(quiz.getQuizId());

        List<QuizQuestionDTO> quizQuestionDTOList = new ArrayList<>();

        if(questionList != null){
            for(Question question : questionList){
                List<QuestionOption> questionOptionList = questionOptionRepo
                        .findAllByQuestionQuestionId(question.getQuestionId());

                QuizQuestionDTO quizQuestionDTO = this.getQuizQuestionDTO(question, questionOptionList);


                quizQuestionDTOList.add(quizQuestionDTO);
            }
        }else {
            throw new ResourceNotFoundException("questions not found");
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println(
                new QuizServeDTO(
                        quiz.getQuizId(),
                        quizQuestionDTOList
                )
        );

        return new QuizServeDTO(
                quiz.getQuizId(),
                quizQuestionDTOList
        );

    }

    private QuizQuestionDTO getQuizQuestionDTO(Question question, List<QuestionOption> questionOptionList) {
        List<QuestionOptionDTO> questionOptionDTOList = new ArrayList<>();

        for(QuestionOption questionOption : questionOptionList){

            QuestionOptionDTO questionOptionDTO = new QuestionOptionDTO();
            questionOptionDTO.setOptionText(questionOption.getOptionText());
            questionOptionDTO.setQuestionOptionId(questionOption.getQuestionOptionId());

            questionOptionDTOList.add(questionOptionDTO);
        }

        QuizQuestionDTO quizQuestionDTO = new QuizQuestionDTO();

        quizQuestionDTO.setQuestionText(question.getQuestionText());
        quizQuestionDTO.setQuestionOptionDTOList(questionOptionDTOList);
        quizQuestionDTO.setQuizQuestionId(question.getQuestionId());
        return quizQuestionDTO;
    }
}
