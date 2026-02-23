package com.educonnect.service.implementation;

import com.educonnect.dto.StudentDetailsDTO;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.StudentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {

    private final StudentRepo studentRepo;
    private final ParentRepo parentRepo;

    @Override
    public StudentDetailsDTO updateStudentDetails(UUID studentUuid,StudentDetailsDTO studentDetailsDTO) {

        Student student = studentRepo
                          .findByStudentUuid(studentUuid)
                          .orElseThrow(() -> new RuntimeException("Student not found"));

        if(studentDetailsDTO.getDateOfBirth()!=null){
            student.setDateOfBirth(studentDetailsDTO.getDateOfBirth());
        }


        if (studentDetailsDTO.getEnrollmentNumber() != null && !studentDetailsDTO.getEnrollmentNumber().isBlank()) {
            student.setEnrollmentNumber(studentDetailsDTO.getEnrollmentNumber().trim());
        }


        if (studentDetailsDTO.getParentUuids() != null) {
            List<Parent> newParents = parentRepo.findByParentUuidIn(studentDetailsDTO.getParentUuids());


            List<Parent> oldParents = new ArrayList<>(student.getParents());
            for(Parent parent : oldParents ){
                student.removeParent(parent);
            }

            for (Parent parent : newParents){
                student.addParent(parent);
            }

        }

        return null;

    }


}
