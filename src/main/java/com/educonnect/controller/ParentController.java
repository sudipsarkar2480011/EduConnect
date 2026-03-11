package com.educonnect.controller;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.service.contract.ParentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
@Tag(name = "06 ParentController")
public class ParentController {
    private final ParentService parentService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyParent(@RequestParam String token){
        parentService.verifyParent(token);
        return ResponseEntity.ok("Parent verified successfully");
    }

    @GetMapping("{parentId}")
    public ResponseEntity<ParentResponseDTO> findParentById(@PathVariable UUID parentId) throws UserNotFoundException {
        return  ResponseEntity.ok(parentService.getById(parentId));
    }

    @GetMapping("/test")
    public  ResponseEntity<String> test(){
        return  ResponseEntity.ok("Working");
    }
}
