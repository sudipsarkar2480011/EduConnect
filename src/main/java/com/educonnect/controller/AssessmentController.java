package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.assessment.AssessmentRequestDTO;
import com.educonnect.dto.assessment.AssignmentRequestDTO;
import com.educonnect.dto.assessment.CreateAssessmentRequestDTO;
import com.educonnect.factory.assessment.AssessmentFactory;
import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/api/assessment")
public class AssessmentController {
    private final AssessmentFactory assessmentFactory;

    @PostMapping("/create")
    public ResponseEntity<String> createAssignment(
            @RequestBody CreateAssessmentRequestDTO dto,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ){

        return new ResponseEntity<>(
                assessmentFactory.createAssessment(
                (Teacher) userPrinciple.getUser(),
                dto),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitAssignment(
            @RequestPart("request") AssessmentRequestDTO dto ,
            @AuthenticationPrincipal UserPrinciples userPrinciple,
            @RequestPart("files") @Nullable  MultipartFile[] files
    ) throws BadRequestException {

       System.out.println("=======================098");


        if(files != null && files.length != 0){
            System.out.println("+++++++++++++++++++++++++++++++  -> " + files.length);
            if(dto != null){
                ((AssignmentRequestDTO) dto).setFiles(Arrays.asList(files));
            }
        }

        System.out.println("-->" +dto);


        return new ResponseEntity<>(
                assessmentFactory.submitAssessment(
                        (Student)userPrinciple.getUser(),
                        dto),
                HttpStatus.CREATED
        );
    }
}
