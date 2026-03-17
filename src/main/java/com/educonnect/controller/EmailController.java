package com.educonnect.controller;

import com.educonnect.service.contract.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController
{
    private final EmailService emailService;

    @GetMapping("send")
    public ResponseEntity<String> sendEmail(@RequestParam String email,@RequestParam String token){
        emailService.sendParentVerificationEmail(email,token);
        return ResponseEntity.ok ("success");
    }
}
