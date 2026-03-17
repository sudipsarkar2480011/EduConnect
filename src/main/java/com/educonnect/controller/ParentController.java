package com.educonnect.controller;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.exception.custom_exceptions.NoChildFoundException;
import com.educonnect.service.contract.parent.ParentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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

    @PostMapping("/send-verification")
    public ResponseEntity<Map<String,Object>> sendVerification(@RequestParam UUID parentId){
        String link= parentService.createParentAndSendVerification(parentId);
        Map<String,Object> response=new HashMap<>();
        response.put("message","Verification Link generated successfully");
        response.put("verification link",link);
        response.put("status","SUCCESS");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{parentId}")
    public ResponseEntity<Void> deleteParent(@PathVariable UUID parentId)  throws UserNotFoundException{
        parentService.delete(parentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{parentId}")
    public ResponseEntity<ParentResponseDTO> findParentById(@PathVariable UUID parentId) throws UserNotFoundException {
        return  ResponseEntity.ok(parentService.getById(parentId));
    }

    @PutMapping("/update")
    public ResponseEntity<ParentResponseDTO> updateParent(@RequestParam UUID parenId, @RequestBody ParentUpdateDTO dto) throws UserNotFoundException{
        ParentResponseDTO response=parentService.update(parenId, dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/link")
    public ResponseEntity<ParentResponseDTO> linkStudent(@RequestParam UUID parentId,@RequestParam UUID studentId) throws NoChildFoundException {
        ParentResponseDTO response=parentService.linkStudent(parentId,studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
