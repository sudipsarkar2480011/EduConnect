package com.educonnect.repo;

import com.educonnect.model.document.DocType;
import com.educonnect.model.document.DocTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocTypeRepo extends JpaRepository<DocType, UUID> {
    Optional<DocType> findByDocTypeName(DocTypeEnum docTypeEnum);
}
