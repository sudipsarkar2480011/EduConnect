package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.result.ResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for handling {@link Result} related requests
 * Delegates business logic for {@link Result} related operations
 */
@RestController
@RequestMapping("/v1/api/result")
@RequiredArgsConstructor
@Tag(name = "10 ResultController")
public class ResultController {

    private final ResultService resultService;

    /**
     * Delivers the result
     * @param assessmentId The id of the assessment whose result is requested
     * @return Map containing data
     */
    @GetMapping("{assessmentId}")
    public ResponseEntity<Map<String, Object>> getResult(
            @PathVariable("assessmentId")UUID assessmentId
            ){
        Result result = resultService.getResultWithId(assessmentId);

        Map<String,Object> map = new HashMap<>();

        map.put("score",result.getPercentageScore());
        map.put("studentName", result.getStudent().getFullName());
        map.put("status", result.getStatus().toString());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * Set the result of an assignment submitted by a student
     * @param assessmentId The id of the assessment
     * @param studentId The id of the Student
     * @param givenScore The given score to the submitted assignment
     * @param userPrinciple The teacher evaluating the assignment
     * @return Map containing data
     */
    @PostMapping("{assessmentId}/student/{studentId}/evaluate")
    public ResponseEntity<Map<String,Object>> setResultByTeacher(
            @PathVariable("assessmentId") UUID assessmentId,
            @PathVariable("studentId") UUID studentId,
            @RequestParam("givenScore") int givenScore,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ){

        String msg = resultService.evaluateStudent(assessmentId,studentId,(Teacher) userPrinciple.getUser(),givenScore);

        Map<String,Object> map = new HashMap<>();
        map.put("message",msg);

        return new ResponseEntity<>(
                map,
                HttpStatus.OK
        );

    }
}
