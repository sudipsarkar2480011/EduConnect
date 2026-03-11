package com.educonnect.controller;

import com.educonnect.service.contract.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling parent-related API operations.
 *
 * This controller provides an endpoint for verifying a parent’s identity
 * using a verification token typically sent via email or SMS.
 *
 * All endpoints are served under the base path: /v1/api/parent.
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/v1/api/parent")
@RequiredArgsConstructor
public class ParentController {
    private ParentService parentService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyParent(@RequestParam String token){
        parentService.verifyParent(token);
        return ResponseEntity.ok("Parent verified successfully");
    }
}
