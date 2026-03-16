package com.educonnect.service.implementation.quiz;

import com.educonnect.dto.assessment.report.quiz.StudentQuestionAttemptDTO;
import com.educonnect.dto.assessment.report.quiz.StudentQuizReportDTO;
import com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.Question;
import com.educonnect.model.assessment.QuestionOption;
import com.educonnect.model.assessment.Quiz;
import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import com.educonnect.repo.assessment.quiz.QuizRepo;
import com.educonnect.repo.assessment.quiz.StudentQuizQuestionResponseRepo;
import com.educonnect.service.contract.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class QuizServiceImpl02 implements QuizService {

    private final QuizRepo quizRepo;
    private final StudentQuizQuestionResponseRepo studentQuizQuestionResponseRepo;

    @Override
    public QuizServeDTO getQuiz(UUID assessmentId) {

        Quiz quiz = quizRepo.findQuizWithQuestionAndOptions(assessmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));
        return mapToQuizServeDTO(quiz);
    }

    private QuizServeDTO mapToQuizServeDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }
        List<QuizQuestionDTO> questionDTOs = quiz.getQuestionList().stream()
                .map(question -> {
                    QuizQuestionDTO qDto = new QuizQuestionDTO();
                    qDto.setQuizQuestionId(question.getQuestionId());
                    qDto.setQuestionText(question.getQuestionText());

                    if (question.getQuestionOptionList() != null) {

                        List<QuestionOptionDTO> optionDTOs
                                = question.getQuestionOptionList()
                                .stream()
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

    @Override
    public StudentQuizReportDTO getQuizReport(UUID submissionId) {

        List<StudentQuizQuestionResponse> studentResponseList
                = studentQuizQuestionResponseRepo.findStudentQuizResponse(submissionId);

        if(studentResponseList == null){
            throw new ResourceNotFoundException("Student response not found");
        }

        StudentQuizReportDTO studentQuizReportDTO = new StudentQuizReportDTO();

        List<StudentQuestionAttemptDTO> studentQuestionAttemptDTO = new ArrayList<>();
        for(StudentQuizQuestionResponse response : studentResponseList){
            studentQuestionAttemptDTO.add(toStudentQuizReportDTO(response));
        }

        studentQuizReportDTO.setStudentQuestionAttemptDTOList(studentQuestionAttemptDTO);
        studentQuizReportDTO.setSubmissionId(submissionId);

        return studentQuizReportDTO;
    }

    private StudentQuestionAttemptDTO toStudentQuizReportDTO(StudentQuizQuestionResponse response){

        StudentQuestionAttemptDTO studentQuestionAttemptDTO
                = new StudentQuestionAttemptDTO();

        Question question = response.getQuestion();

        Set<QuestionOption> questionOptionSet = question.getQuestionOptionList();

        for(QuestionOption option : questionOptionSet){
            if(option.getIsCorrectOption()){
                studentQuestionAttemptDTO.setCorrectOptionId(option.getQuestionOptionId());
                studentQuestionAttemptDTO.setCorrectOptionText(option.getOptionText());
            }
            if(option.getQuestionOptionId().equals(response.getQuestionOption().getQuestionOptionId())){
                studentQuestionAttemptDTO.setChosenOptionId(option.getQuestionOptionId());
                studentQuestionAttemptDTO.setChosenOptionText(option.getOptionText());
            }
        }

        studentQuestionAttemptDTO.setIsCorrect(response.getIsCorrectOptionChosen());
        studentQuestionAttemptDTO.setQuestionId(question.getQuestionId());
        studentQuestionAttemptDTO.setQuestionText(question.getQuestionText());

        return studentQuestionAttemptDTO;
    }
}
