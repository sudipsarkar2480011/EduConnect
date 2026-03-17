package com.educonnect.service.implementation.parent;

import com.educonnect.config.JWTService;
import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.exception.custom_exceptions.NoChildFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.token.ParentVerificationToken;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.parent.ParentRepo;
import com.educonnect.repo.parent.ParentVerificationTokenRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.EmailService;
import com.educonnect.service.contract.parent.ParentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepo parentRepo;
    private final StudentRepo studentRepo;
    private final ParentVerificationTokenRepo tokenRepo;
    private final JWTService jwtService;
    private final EmailService emailService;



    @Override
    public ParentResponseDTO getById(UUID id) throws UserNotFoundException {
        Parent parent = parentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Parent not found: " + id));
        return toResponse(parent);
    }

    @Override
    @Transactional
    public ParentResponseDTO update(UUID id, ParentUpdateDTO dto) throws UserNotFoundException {
        Parent parent = parentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Parent not found: " + id));

        // Update scalar fields (aligns with your typical User/Teacher patterns)
        if (dto.getName() != null)     parent.setFullName(dto.getName());
        if (dto.getContactInfo() != null)  parent.setPhoneNumber(dto.getContactInfo());

        Parent saved = parentRepo.save(parent);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws UserNotFoundException {
        parentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public ParentResponseDTO linkStudent(UUID parentId, UUID studentId) throws NoChildFoundException {
        // The method contract only declares NoChildFoundException.
        // Use it for not-found scenarios in this method.
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new NoChildFoundException("Parent not found: " + parentId));
        //if(!Boolean.TRUE.equals(parent.getVerified())){
            //throw new RuntimeException("Parent is not verified yet");
       // }
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NoChildFoundException("Student not found: " + studentId));

        student.setParent(parent);
        studentRepo.save(student);

        // Optionally refresh parent to ensure linked list is up to date
        Parent refreshed = parentRepo.findById(parentId)
                .orElseThrow(() -> new NoChildFoundException("Parent not found after linking: " + parentId));

        return toResponse(refreshed);
    }


 public void createParentAndSendVerification(UUID parentId) {
        Parent parent=parentRepo.findById(parentId).orElseThrow(()->new RuntimeException("Parent Not found"));
        String parentEmail= parent.getEmail();
        String token=jwtService.generateToken(parentEmail);
        ParentVerificationToken verificationToken=ParentVerificationToken.builder().
                token(token).
                parent(parent).
                expiryDate(LocalDateTime.now().plusHours(24)).
                build();
             tokenRepo.save(verificationToken);
    }



/**
 * Verifies a parent account using a previously issued verification token.
 *

 *Flow:

 *   Look up {@link com.educonnect.model.token.ParentVerificationToken} by token
            *   Validate that the token has not expired
            *   Mark the associated {@link com.educonnect.model.user.Parent} as verified
            *   Persist the parent update
            *   Delete the used verification token

            * <p>Security considerations:
 *   Reject expired tokens
            *   Ensure tokens are single-use by deleting after success
            *   Consider rotating or invalidating older tokens if multiple are issued

            * @param token the verification token received by the parent
 * @throws RuntimeException if the token is invalid (not found) or expired
 */

@Override
@Transactional
    public void verifyParent(String token) {
        ParentVerificationToken tokenObj=tokenRepo.findByToken(token).orElseThrow(()->new RuntimeException("Invalid token"));
        if(tokenObj.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }
        Parent parent=tokenObj.getParent();
        parent.setVerified(true);
        parentRepo.save(parent);
        tokenRepo.delete(tokenObj);
    }

    // -----------------------------
    // Mapper
    // -----------------------------
    private ParentResponseDTO toResponse(Parent p) {
        ParentResponseDTO dto = new ParentResponseDTO();
        dto.setId(p.getUserId());
        dto.setName(p.getFullName());
        dto.setContactInfo(p.getPhoneNumber());
          return dto;
    }


}