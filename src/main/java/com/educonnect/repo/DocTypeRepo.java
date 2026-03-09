package com.educonnect.repo;

import com.educonnect.model.document.DocType;
import com.educonnect.model.document.DocTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocTypeRepo extends JpaRepository<DocType, UUID> {
    Optional<DocType> findByDocTypeName(DocTypeEnum docTypeEnum);
}
