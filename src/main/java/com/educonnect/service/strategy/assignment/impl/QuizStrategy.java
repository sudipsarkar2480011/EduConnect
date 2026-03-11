package com.educonnect.service.strategy.assignment.impl;

import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.assessment.create.quiz.CreateQuizRequestDTO;
import com.educonnect.dto.assessment.create.quiz.QuestionOptionDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.create.quiz.QuizQuestionDTO;
import com.educonnect.dto.assessment.submit.quiz.StudentQuestionAndAnswerDTO;
import com.educonnect.dto.assessment.submit.quiz.StudentQuizQuestionResponseDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.*;
import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.model.user.User;
import com.educonnect.repo.assessment.AssessmentRepo;
import com.educonnect.repo.assessment.SubmissionRepo;
import com.educonnect.repo.assessment.quiz.QuestionOptionRepo;
import com.educonnect.repo.assessment.quiz.QuestionRepo;
import com.educonnect.repo.assessment.quiz.QuizRepo;
import com.educonnect.repo.assessment.quiz.StudentQuizQuestionResponseRepo;
import com.educonnect.repo.course.CourseRepo;
import com.educonnect.service.contract.result.ResultService;
import com.educonnect.service.strategy.assignment.AssessmentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
/**
 * Implementation of AssignmentStrategy for {@link AssessmentType} QUIZ
 * @author SudipSarkar
 * @version 1.0
 * @since 1.0
 */
public class QuizStrategy implements AssessmentStrategy {

    private final CourseRepo courseRepo;
    private final QuizRepo quizRepo;
    private final QuestionRepo questionRepo;
    private final QuestionOptionRepo questionOptionRepo;
    private final AssessmentRepo assessmentRepo;
    private final SubmissionRepo submissionRepo;
    private final StudentQuizQuestionResponseRepo studentQuizQuestionResponseRepo;
    private final ResultService resultService;

    @Override
    public boolean supports(AssessmentType type) {
        return type.toString().equals("QUIZ") || type.toString().equals("QUIZ_SUBMISSION");
    }


    @Override
    @Transactional
    public String createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO) {

        Course course = courseRepo.findById(assessmentRequestDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Assessment assessment =
                Assessment.builder()
                        .maxScore(assessmentRequestDTO.getMaxScore())
                        .title(assessmentRequestDTO.getTitle())
                        .type(assessmentRequestDTO.getAssessmentType())
                        .noOfStudentSubmitted(0)
                        .course(course)
                        .build();

        assessmentRepo.save(assessment);


        Quiz quiz = new Quiz();
        quiz.setAssessment(assessment);
        quizRepo.save(quiz);


        List<QuizQuestionDTO> quizRequestDTOList =
                ((CreateQuizRequestDTO)assessmentRequestDTO).getQuestionDTOList();

        List<Question> questionList = new ArrayList<>();
        List<QuestionOption> questionOptionList = new ArrayList<>();

        for (QuizQuestionDTO quizQuestionDTO : quizRequestDTOList){

            List<QuestionOptionDTO> questionOptionDTOList = quizQuestionDTO.getQuestionOptions();

            Question question =
                    Question.builder()
                            .questionText(quizQuestionDTO.getQuestionText())
                            .quiz(quiz)
                            .build();

            questionList.add(question);

            for (QuestionOptionDTO questionOptionDTO : questionOptionDTOList){

                QuestionOption questionOption =
                        QuestionOption.builder()
                                .optionText(questionOptionDTO.getOptionText())
                                .isCorrectOption(questionOptionDTO.getIsCorrectOption())
                                .question(question)
                                .build();

                questionOptionList.add(questionOption);

            }
        }

        questionRepo.saveAll(questionList);
        questionOptionRepo.saveAll(questionOptionList);

        return "Quiz with title : " + assessment.getTitle() + " saved Successfully";
    }

    @Override
    @Transactional
    public String submitAssessment(Student student, AssessmentRequestDTO assessmentRequestDTO) {

        StudentQuizQuestionResponseDTO dto = (StudentQuizQuestionResponseDTO) assessmentRequestDTO;

        Assessment assessment = assessmentRepo.findById(dto.getAssessmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Assessment not found"));

        if(submissionRepo.existsByStudentAndAssessment(student, assessment)){
            try {
                throw new BadRequestException("You already submitted the Quiz");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        Quiz quiz = quizRepo.findById(dto.getQuizId())
                .orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));


        Submission submission =
                Submission.builder()
                        .student(student)
                        .assessment(assessment)
                        .submissionStatus(SubmissionStatus.NOT_SUBMITTED)
                        .build();

        submissionRepo.save(submission);

        List<StudentQuestionAndAnswerDTO> studentQuestionAndAnswerDTOList = dto.getStudentQuestionAndAnswerDTOList();

        List<StudentQuizQuestionResponse> studentQuizQuestionResponseList = new ArrayList<>();

        for(StudentQuestionAndAnswerDTO studentQuestionAndAnswerDTO : studentQuestionAndAnswerDTOList){

            StudentQuizQuestionResponse studentQuizQuestionResponse = new StudentQuizQuestionResponse();
            studentQuizQuestionResponse.setQuiz(quiz);
            studentQuizQuestionResponse.setSubmission(submission);

            Question question = questionRepo.findById(studentQuestionAndAnswerDTO.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found!"));

            QuestionOption questionOption = questionOptionRepo.findById(studentQuestionAndAnswerDTO.getQuestionOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Option not found not found!"));

            studentQuizQuestionResponse.setQuestion(question);
            studentQuizQuestionResponse.setQuestionOption(questionOption);

            studentQuizQuestionResponse.setIsCorrectOptionChosen(questionOption.getIsCorrectOption());

            studentQuizQuestionResponseList.add(studentQuizQuestionResponse);

        }

        submission.setSubmissionStatus(SubmissionStatus.SUBMITTED);

        studentQuizQuestionResponseRepo.saveAll(studentQuizQuestionResponseList);

        String msg = resultService.computeQuizResult(assessment.getAssessmentId(),student.getUserId());

        log.info("Message from resultService : {}",msg );
        log.info("Result computed successfully for quiz : {}", quiz.getQuizId());

        return "Quiz submitted successfully";

    }

}
