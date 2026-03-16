package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.assessment.report.quiz.StudentQuizReportDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.submit.assignment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.common.GenericResponse;
import com.educonnect.factory.assessment.AssessmentFactory;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.quiz.QuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/assessment")
@Tag(name = "09 AssessmentController")
/**
 * REST controller for handling assessment related requests
 * Delegates business logic for assessment related operations
 */
public class AssessmentController {
    private final AssessmentFactory assessmentFactory;
    private final QuizService quizService;

    /**
     *
     * @param dto The payload
     * @param userPrinciple The AuthenticationPrincipal
     * @return Success message
     */

    @PostMapping("/create")
    public ResponseEntity<Map<String,String>> createAssignment(
            @RequestBody CreateAssessmentRequestDTO dto,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ) throws BadRequestException{

        return new ResponseEntity<>(
                assessmentFactory.createAssessment(
                (Teacher) userPrinciple.getUser(),
                dto),
                HttpStatus.CREATED
        );
    }

    /**
     *
     * @param dto The payload
     * @param userPrinciple AuthenticationPrincipal
     * @param files The file data (null or empty in case of Quiz submission)
     * @return Success message
     * @throws BadRequestException
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String,String>> submitAssignment(
            @RequestPart("request") AssessmentRequestDTO dto ,
            @AuthenticationPrincipal UserPrinciples userPrinciple,
            @RequestPart("files") @Nullable  MultipartFile[] files
    ) throws BadRequestException {

        if(files != null && files.length != 0){
            if(dto != null){
                ((AssignmentRequestDTO) dto).setFiles(Arrays.asList(files));
            }
        }

        return new ResponseEntity<>(
                assessmentFactory.submitAssessment(
                        (Student)userPrinciple.getUser(),
                        dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/quiz/{assessmentId}")
    public ResponseEntity<GenericResponse<QuizServeDTO>> serveQuiz(
            @PathVariable("assessmentId") UUID assessmentId
            ){
        return ResponseEntity.ok(
                new GenericResponse<>(
                        quizService.getQuiz(assessmentId),
                        "Quiz retrieved successfully",
                        HttpStatus.OK.value()
                )
        );
    }

    @GetMapping("/quiz/report/{submissionId}")
    private ResponseEntity<GenericResponse<StudentQuizReportDTO>> getReport(
            @PathVariable("submissionId") UUID submissionId
    ){
        return ResponseEntity.ok(
                new GenericResponse<>(
                        quizService.getQuizReport(submissionId),
                        "Quiz report retrieved successfully",
                        HttpStatus.OK.value()
                )
        );
    }
}
