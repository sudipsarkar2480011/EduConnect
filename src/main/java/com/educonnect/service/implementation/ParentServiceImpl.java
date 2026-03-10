package com.educonnect.service.implementation;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.exception.custom_exceptions.NoChildFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.ParentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepo parentRepo;
    private final StudentRepo studentRepo;


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
        Parent parent = parentRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Parent not found: " + id));

        // Unlink children first to avoid FK constraint issues
//        List<Student> linked = parent.getLinkedStudents();
//        if (linked != null && !linked.isEmpty()) {
//            for (Student s : linked) {
//                s.setParent(null);
//            }
//            studentRepo.saveAll(linked);
//        }

        parentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public ParentResponseDTO linkStudent(UUID parentId, UUID studentId) throws NoChildFoundException {
        // The method contract only declares NoChildFoundException.
        // Use it for not-found scenarios in this method.
        Parent parent = parentRepo.findById(parentId)
                .orElseThrow(() -> new NoChildFoundException("Parent not found: " + parentId));

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new NoChildFoundException("Student not found: " + studentId));

        student.setParent(parent);
        studentRepo.save(student);

        // Optionally refresh parent to ensure linked list is up to date
        Parent refreshed = parentRepo.findById(parentId)
                .orElseThrow(() -> new NoChildFoundException("Parent not found after linking: " + parentId));

        return toResponse(refreshed);
    }

    // -----------------------------
    // Mapper
    // -----------------------------
    private ParentResponseDTO toResponse(Parent p) {
        ParentResponseDTO dto = new ParentResponseDTO();
        dto.setId(p.getUserId());
        dto.setName(p.getFullName());
        dto.setContactInfo(p.getPhoneNumber());
        //dto.setStatus(p.get());

//        List<Student> children = p.getLinkedStudents();
//        dto.setLinkedStudentIds(
//                (children == null) ? List.of() :
//                        children.stream()
//                                .map(Student::getUserId)
//                                .collect(Collectors.toList())
//        );
          return dto;
    }
}