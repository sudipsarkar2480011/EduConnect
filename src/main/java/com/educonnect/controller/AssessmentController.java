package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.dto.assessment.CreateAssessmentRequestDTO;
import com.educonnect.factory.assessment.AssessmentFactory;
import com.educonnect.model.user.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/api/assessment")
public class AssessmentController {
    private final AssessmentFactory assessmentFactory;

    @PostMapping
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
}
