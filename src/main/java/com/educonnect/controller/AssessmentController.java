package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.assessment.report.AssessmentReportDTO;
import com.educonnect.dto.assessment.report.quiz.StudentQuizReportDTO;
import com.educonnect.dto.assessment.serve.AssessmentServeDTO;
import com.educonnect.dto.assessment.serve.quiz.QuizServeDTO;
import com.educonnect.dto.assessment.submit.AssessmentRequestDTO;
import com.educonnect.dto.assessment.submit.assignment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.create.CreateAssessmentRequestDTO;
import com.educonnect.dto.common.GenericResponse;
import com.educonnect.factory.assessment.AssessmentFactory;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    @GetMapping("/get-assessment/{assessmentType}/{assessmentId}")
    public ResponseEntity<GenericResponse<AssessmentServeDTO>> serveAssessment(
            @PathVariable("assessmentId") UUID assessmentId,
            @PathVariable("assessmentType") String assessmentType,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ) throws BadRequestException {
        return ResponseEntity.ok(
                new GenericResponse<>(
                        assessmentFactory.serveAssessment(assessmentId,assessmentType,userPrinciple.getUser()),
                        "Assessment [" +assessmentType.toLowerCase()+ "] retrieved successfully",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        );
    }

    @GetMapping("/report/{assessmentType}/{submissionId}")
    private ResponseEntity<GenericResponse<AssessmentReportDTO>> getReport(
            @PathVariable("submissionId") UUID submissionId,
            @AuthenticationPrincipal UserPrinciples userPrinciple,
            @PathVariable("assessmentType") String assessmentType
    ) throws BadRequestException {
        return ResponseEntity.ok(
                new GenericResponse<>(
                        assessmentFactory.getReport(submissionId, userPrinciple.getUser(),assessmentType),
                        "Assessment [" +assessmentType.toLowerCase()+ "] report retrieved successfully",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        );
    }
}
