package com.educonnect.service.contract.parent;

import com.educonnect.dto.parent.ParentResponseDTO;
import com.educonnect.dto.parent.ParentUpdateDTO;
import com.educonnect.exception.custom_exceptions.NoChildFoundException;
import com.educonnect.exception.custom_exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * Service interface for handling Parent-related operations.
 * Defines the contract for managing parent profiles and their associations with students.
 *
 * @author Sankha Subhra Chakraborty
 * @version 1.0
 * @since 1.0
 */
public interface ParentService {

    /**
     * Retrieves the details of a specific parent by their unique identifier.
     *
     * @param id The unique identifier of the parent
     * @return {@link ParentResponseDTO} The data transfer object containing parent details
     * @throws UserNotFoundException If no parent is found with the provided ID
     * @since 1.0
     */
    ParentResponseDTO getById(UUID id) throws UserNotFoundException;

    /**
     * Updates an existing parent's information in the system.
     *
     * @param parentId The unique identifier of the parent to update
     * @param dto The data transfer object containing the updated parent information
     * @return {@link ParentResponseDTO} The updated parent record
     * @throws UserNotFoundException If the parent record does not exist
     * @since 1.0
     */
    ParentResponseDTO update(UUID parentId, ParentUpdateDTO dto) throws UserNotFoundException;

    /**
     * Permanently deletes a parent record from the system.
     *
     * @param id The unique identifier of the parent to be removed
     * @throws UserNotFoundException If no parent is found with the provided ID
     * @since 1.0
     */
    void delete(UUID id) throws UserNotFoundException;

    /**
     * Establishes a relationship link between a parent and a student.
     * This allows parents to access their children's academic data.
     *
     * @param parentId The unique identifier of the parent
     * @param studentId The unique identifier of the student to be linked
     * @return {@link ParentResponseDTO} The updated parent record including the new student link
     * @throws NoChildFoundException If the specified student cannot be found to complete the link
     * @since 1.0
     */
    ParentResponseDTO linkStudent(UUID parentId, UUID studentId) throws NoChildFoundException;
    String createParentAndSendVerification(UUID parentId);
    ResponseEntity<String> verifyParent(String token);
}