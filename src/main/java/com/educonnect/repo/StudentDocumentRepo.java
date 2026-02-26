package com.educonnect.repo;

import com.educonnect.model.document.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Repository for communication with student database
 * @author sudipsarkar
 * @version 1.0
 * @since 1.0
 *
 */

@Repository
public interface StudentDocumentRepo extends JpaRepository<StudentDocument,Long> {

    /**
     *
     * save the student document in the DB (BLOB)
     * @param documentUuid The unique identifier to find the student document from DB
     * @return {@link Optional}<{@link StudentDocument}>
     * @since 1.0
     */
    Optional<StudentDocument> findByStudentDocumentId(UUID documentUuid);
}
