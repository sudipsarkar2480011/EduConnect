package com.educonnect.service.implementation;

import com.educonnect.dto.studentdetails.StudentDetailsDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.parent.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.parent.ParentService;
import com.educonnect.service.contract.StudentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {

    private final StudentRepo studentRepo;
    private final ParentRepo parentRepo;
    private final ParentService parentService;

    @Override
    public StudentDetailsDTO updateStudentDetails(UUID studentUuid,StudentDetailsDTO studentDetailsDTO) throws UserNotFoundException {

        Student student = studentRepo
                          .findById(studentUuid)
                          .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if(studentDetailsDTO.getDateOfBirth()!=null){
            student.setDateOfBirth(studentDetailsDTO.getDateOfBirth());
        }
        if(studentDetailsDTO.getParentEmail()!=null && studentDetailsDTO.getParentEmail().isBlank()){
            parentService.createParentAndSendVerification(studentDetailsDTO.getParentId());
        }

        if (studentDetailsDTO.getEnrollmentNumber() != null && !studentDetailsDTO.getEnrollmentNumber().isBlank()) {
            student.setEnrollmentNumber(studentDetailsDTO.getEnrollmentNumber().trim());
        }


        if (studentDetailsDTO.getParentId() != null) {
            Parent parent=parentRepo.findById(studentDetailsDTO.getParentId()).orElseThrow(()->new UserNotFoundException("Parent not found"));
            student.setParent(parent);
        }
        Student savedStudent=studentRepo.save(student);
        return StudentDetailsDTO.builder().
                dateOfBirth(savedStudent.getDateOfBirth()).
                enrollmentNumber(savedStudent.getEnrollmentNumber()).
                parentId(savedStudent.getParent()!=null?savedStudent.getParent().getUserId():null).
                build(); //YET TO implement

    }


}
