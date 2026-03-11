package com.educonnect.service.implementation;

import com.educonnect.dto.parent.ParentAccessRequestDTO;
import com.educonnect.dto.parent.ParentAccessResponseDTO;
import com.educonnect.model.access.ParentAccess;
import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.educonnect.repo.ParentAccessRepo;
import com.educonnect.repo.ParentRepo;
import com.educonnect.repo.StudentRepo;
import com.educonnect.service.contract.ParentAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


/**
 * Implementation of  ParentAccessService that manages granting and retrieving
 * parent access permissions to student information.
 * This service handles:
 *     Validating the existence of parent and student records
 *     Ensuring duplicate access permissions are not created
 *     Persisting new access records to the database
 *     Fetching existing access permissions
 * All business logic related to parent-student access associations
 * is processed within this class.
 *
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

@Service
@RequiredArgsConstructor
public class ParentAccessServiceImpl implements ParentAccessService {
    private final ParentAccessRepo parentAccessRepo;
    private final ParentRepo parentRepo;
    private final StudentRepo studentRepo;

 /** Grants a parent access permission to view or interact with a student’s information.
            *
            * This method performs the following:
            *
            * Validates whether the parent exists
            *     Validates whether the student exists
            *     Checks if access already exists between the parent and student
            *     Creates and saves a new {@link ParentAccess} record
            *     Returns a {@link ParentAccessResponseDTO} containing saved details
            *
            *
            * @param request the DTO containing parent ID, student ID,
            *                permissions, and access status
     * @return a response DTO containing the newly created access record
     * @throws RuntimeException if parent or student does not exist,
     *                          or if access already exists
     */

    @Override
    public ParentAccessResponseDTO grantAccess(ParentAccessRequestDTO request) {
        Parent parent = parentRepo.findById(request.getParentId()).orElseThrow(() -> new RuntimeException("Parent not found"));
        Student student=studentRepo.findById(request.getStudentId()).orElseThrow(()->new RuntimeException("Student not found"));
        Optional<ParentAccess> existing=parentAccessRepo.findByParentUserIdAndStudentUserId(request.getParentId(),request.getStudentId());
        if(existing.isPresent()){
            throw new RuntimeException("Access already exists for this parent");
        }
        ParentAccess access=new ParentAccess();
        access.setParent(parent);
        access.setStudent(student);
        access.setPermission(request.getPermissions());
        access.setStatus(request.getStatus());
        ParentAccess saved=parentAccessRepo.save(access);
        return ParentAccessResponseDTO.builder().
                accessId(saved.getParentAccessId()).
                parentId(parent.getUserId()).
                studentId(student.getUserId()).
                permissions(saved.getPermission()).
                status(saved.getStatus()).
                build();
    }

    /**
     * Retrieves the access permission associated with a parent and a student.
     * It validates whether such an access record exists. If found, it maps the
     * entity into a {@link ParentAccessResponseDTO} and returns it.
     * @param parentId  the unique identifier of the parent
     * @param studentId the unique identifier of the student
     * @return a response DTO containing access permissions and status
     * @throws RuntimeException if no access record exists for the given parent-student pair
     */

    @Override
    public ParentAccessResponseDTO getAccess(UUID parentId,UUID studentId){
        ParentAccess access=parentAccessRepo.findByParentUserIdAndStudentUserId(parentId,studentId).orElseThrow(()->new RuntimeException("Access not found"));
        return ParentAccessResponseDTO.builder().
                accessId(access.getParentAccessId()).
                parentId(parentId).
                studentId(studentId).
                permissions(access.getPermission()).
                status(access.getStatus()).
                build();
    }

}
