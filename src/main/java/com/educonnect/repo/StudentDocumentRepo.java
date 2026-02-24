package com.educonnect.repo;

import com.educonnect.model.document.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentDocumentRepo extends JpaRepository<StudentDocument,Long> {

    Optional<StudentDocument> findByDocumentUuid(UUID documentUuid);
}
