package com.educonnect.repo;

import com.educonnect.model.document.DocType;
import com.educonnect.model.document.DocTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocTypeRepo extends JpaRepository<DocType, Long> {
    Optional<DocType> findByDocTypeName(DocTypeEnum docTypeEnum);
}
