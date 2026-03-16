package com.educonnect.service.strategy.assessment.impl;

import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.assessment.create.quiz.CreateQuizRequestDTO;
import com.educonnect.dto.assessment.create.quiz.QuestionOptionDTO;
import com.educonnect.dto.assessment.report.AssessmentReportDTO;
import com.educonnect.dto.assessment.report.quiz.StudentQuestionAttemptDTO;
import com.educonnect.dto.assessment.report.quiz.StudentQuizReportDTO;
import com.educonnect.dto.assessment.serve.AssessmentServeDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.create.quiz.QuizQuestionDTO;
import com.educonnect.dto.assessment.submit.quiz.StudentQuestionAndAnswerDTO;
import com.educonnect.dto.assessment.submit.quiz.StudentQuizQuestionResponseDTO;
import com.educonnect.exception.custom_exceptions.ResourceNotFoundException;
import com.educonnect.model.assessment.*;
import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import com.educonnect.model.course.Course;
import com.educonnect.model.user.Role;
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
import com.educonnect.service.strategy.assessment.AssessmentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Map<String,String> createAssessment(Teacher teacher, CreateAssessmentRequestDTO assessmentRequestDTO) throws BadRequestException {

        Course course = courseRepo.findById(assessmentRequestDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if(!canCreateAssessment(teacher,course)){
            throw new BadRequestException("Teacher " + teacher.getFullName() +" can't add assessment to this course " + course.getTitle());
        }

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

        Map<String,String> map = new HashMap<>();
        map.put("message","Quiz created successfully");
        map.put("assessmentId",assessment.getAssessmentId().toString());
        map.put("quizId", quiz.getQuizId().toString());

        return map;
    }

    @Override
    public AssessmentServeDTO serveAssessment(UUID assessmentId) {
        Quiz quiz = quizRepo.findQuizWithQuestionAndOptions(assessmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Quiz not found"));
        return mapToQuizServeDTO(quiz);
    }

    private QuizServeDTO mapToQuizServeDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }
        List<com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO> questionDTOs = quiz.getQuestionList().stream()
                .map(question -> {
                    com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO qDto = new com.educonnect.dto.assessment.serve.quiz.QuizQuestionDTO();
                    qDto.setQuizQuestionId(question.getQuestionId());
                    qDto.setQuestionText(question.getQuestionText());

                    if (question.getQuestionOptionList() != null) {

                        List<com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO> optionDTOs = question.getQuestionOptionList()
                                .stream()
                                .map(option -> {
                                    com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO oDto = new com.educonnect.dto.assessment.serve.quiz.QuestionOptionDTO();
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
        QuizServeDTO quizServeDTO = new QuizServeDTO(quiz.getQuizId(), questionDTOs);

        ((AssessmentServeDTO)quizServeDTO).setAssessmentType(AssessmentType.QUIZ);
        ((AssessmentServeDTO)quizServeDTO).setTitle(quiz.getAssessment().getTitle());

        return quizServeDTO;
    }


    @Override
    public AssessmentReportDTO getReport(UUID submissionId, User user) throws BadRequestException {
        List<StudentQuizQuestionResponse> studentResponseList
                = studentQuizQuestionResponseRepo.findStudentQuizResponse(submissionId);

        if(studentResponseList == null || studentResponseList.isEmpty()){
            throw new ResourceNotFoundException("Student response not found");
        }

        if(!(user.getRole().equals(Role.ADMIN)
                ||
                user.getUserId()
                        .equals(studentResponseList.getFirst().getSubmission().getStudent().getUserId()))){
            throw new BadRequestException("Student " + user.getFullName()
                    + " is not authorized to access this report");
        }


        StudentQuizReportDTO studentQuizReportDTO = new StudentQuizReportDTO();

        List<StudentQuestionAttemptDTO> studentQuestionAttemptDTO = new ArrayList<>();
        for(StudentQuizQuestionResponse response : studentResponseList){
            studentQuestionAttemptDTO.add(toStudentQuizReportDTO(response));
        }

        studentQuizReportDTO.setStudentQuestionAttemptDTOList(studentQuestionAttemptDTO);
        studentQuizReportDTO.setSubmissionId(submissionId);

        ((AssessmentReportDTO)studentQuizReportDTO).setAssessmentType(AssessmentType.QUIZ);
        ((AssessmentReportDTO)studentQuizReportDTO).setTitle(studentResponseList.getFirst().getSubmission().getAssessment().getTitle());
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

    @Override
    @Transactional
    public Map<String,String> submitAssessment(Student student, AssessmentRequestDTO assessmentRequestDTO) {

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

        Map<String,String> map = new HashMap<>();

        map.put("message", "Attempted the Quiz with id" + quiz.getQuizId());
        map.put("assessmentId", assessment.getAssessmentId().toString());
        map.put("quizId", quiz.getQuizId().toString());

        return map;

    }

}
