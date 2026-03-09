package com.educonnect.controller;

import com.educonnect.config.UserPrinciples;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.user.Teacher;
import com.educonnect.service.contract.result.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

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

    @PostMapping("{assessmentId}/student/{studentId}/evaluate")
    public ResponseEntity<Object> setResultByTeacher(
            @PathVariable("assessmentId") UUID assessmentId,
            @PathVariable("studentId") UUID studentId,
            @RequestParam("givenScore") int givenScore,
            @AuthenticationPrincipal UserPrinciples userPrinciple
            ){

        String msg = resultService.evaluateStudent(assessmentId,studentId,(Teacher) userPrinciple.getUser(),givenScore);

        return new ResponseEntity<>(
                new HashMap<>().put("message",msg),
                HttpStatus.OK
        );

    }
}
