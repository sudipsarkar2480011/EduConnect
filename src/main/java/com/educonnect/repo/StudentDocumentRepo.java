package com.educonnect.repo;

import com.educonnect.model.document.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDocumentRepo extends JpaRepository<StudentDocument,Long> {
}
