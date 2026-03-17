package com.educonnect.service.contract.parent;

import com.educonnect.dto.parent.ParentAccessRequestDTO;
import com.educonnect.dto.parent.ParentAccessResponseDTO;

import java.util.UUID;

/**
 * Service interface for managing access permissions that allow parents
 * to view or interact with student information.
 *
 * This interface defines operations for:
 *
 *     Granting access permissions from a parent to a student
 *     Retrieving existing access permissions
 *
 *
 *The implementing class is expected to handle validation,
 * business logic, and mapping between entities and DTOs.
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

public interface ParentAccessService {

    /**
     * Grants a parent access to a student’s information.
     *
     * This operation typically includes:
     *     Validating the existence of the parent and student
     *     Ensuring no existing access record already exists
     *     Creating and persisting a new access permission
     *
     * @param request the request body containing parent ID, student ID,
     *                permissions, and access status
     * @return a {@link ParentAccessResponseDTO} representing the created access record
     */

    ParentAccessResponseDTO grantAccess(ParentAccessRequestDTO request);

    /**
     * Retrieves an existing access permission between a parent and a student.
     *
     *This method is used to check if a parent has permission to access
     * a student’s data and returns the corresponding access details.
     *
     * @param parentId  the parent's unique identifier
     * @param studentId the student's unique identifier
     * @return a {@link ParentAccessResponseDTO} containing the stored access details
     */

    ParentAccessResponseDTO getAccess(UUID parentId,UUID studentId);
}
