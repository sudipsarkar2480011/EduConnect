package com.educonnect.controller;


import com.educonnect.service.contract.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController
{
    @Autowired
    private EmailService emailService;
    @GetMapping
    public ResponseEntity<String> sendEmail(){
        emailService.sendParentVerificationEmail("dipakrudra8@gmail.com","adadandankabfiaoxaouxsakbdkabdkhsad");
        return  ResponseEntity.ok("success");
    }
}
