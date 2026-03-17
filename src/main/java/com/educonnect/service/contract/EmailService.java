package com.educonnect.service.contract;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending application emails.
 *
 * Currently supports dispatching the parent verification email that contains
 * a time-bound verification token. Uses Spring's {@link JavaMailSender} abstraction,
 * which delegates to the configured mail transport (e.g., SMTP).
 *
 * @version 1.0
 * @since 1.0
 */

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

/**
 * Sends a parent verification email to the provided address, including a
 * verification link that contains the token as a query parameter.
 *
 * The verification URL is currently constructed as:

 * http://localhost:8081/v1/api/parent/verify?token={token}

 *
 * Implementation details:

 *     Builds a {@link SimpleMailMessage} with recipient, subject, and plain-text body
 *     Delegates to {@link JavaMailSender#send(org.springframework.mail.SimpleMailMessage)}
 *
 *
 * @param email the recipient email address
 * @param token the verification token to be embedded in the verification link
 * @throws org.springframework.mail.MailException if sending fails due to transport or

configuration issues
 */

public void sendParentVerificationEmail(String email,String token) {
        System.out.println(email);
        String verifyUrl = "http://localhost:8081/v1/api/parent/verify?token=" + token;
        SimpleMailMessage  message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Educonnect Parent Verification");
        message.setText("Click the link to verify your account:\n"+verifyUrl);
        mailSender.send(message);
    }
}
