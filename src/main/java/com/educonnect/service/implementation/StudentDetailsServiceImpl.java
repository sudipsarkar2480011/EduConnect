package com.educonnect.service.implementation;

import com.educonnect.dto.studentdetails.StudentDetailsDTO;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Student;
import com.educonnect.repo.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {

    private final StudentRepo studentRepo;
    private final ParentRepo parentRepo;

    @Override
    public StudentDetailsDTO updateStudentDetails(UUID studentUuid,StudentDetailsDTO studentDetailsDTO) throws UserNotFoundException {

        Student student = studentRepo
                          .findById(studentUuid)
                          .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if(studentDetailsDTO.getDateOfBirth()!=null){
            student.setDateOfBirth(studentDetailsDTO.getDateOfBirth());
        }


        if (studentDetailsDTO.getEnrollmentNumber() != null && !studentDetailsDTO.getEnrollmentNumber().isBlank()) {
            student.setEnrollmentNumber(studentDetailsDTO.getEnrollmentNumber().trim());
        }


        if (studentDetailsDTO.getParentId() != null) {

        }

        return null; //YET TO implement

    }


}
