package com.educonnect.repo;

import com.educonnect.model.access.ParentAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link ParentAccess} entities.
 *
 *This interface extends {@link JpaRepository} to provide CRUD and pagination
 * operations for parent access records. It also includes a custom query method
 * for retrieving access permissions based on a parent ID and student ID.
 *
 *The repository primarily supports:
 *     Fetching access permissions linking a parent and student
 *     Checking if access already exists before creating new permissions
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

public interface ParentAccessRepo extends JpaRepository<ParentAccess, UUID> {

    /**
     * Finds a {@link ParentAccess} record based on the parent’s user ID
     * and the student’s user ID.
     *
     * This method is typically used to:
     *     Verify whether a parent already has access to a student's information
     *     Retrieve existing access permissions
     *
     * @param parentId  the unique identifier of the parent
     * @param studentId the unique identifier of the student
     * @return an {@link Optional} containing the matching {@link ParentAccess} record,
     *         or empty if no such permission exists
     */

    Optional<ParentAccess> findByParentUserIdAndStudentUserId(UUID parentId,UUID studentId);
}
