package com.educonnect.repo.result;

import com.educonnect.model.assessment.Result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepo extends JpaRepository<Result, UUID> {
    Optional<Result> findByAssessmentAssessmentId(UUID assessmentId);
    boolean existsByAssessmentAssessmentId(UUID assessmentId);
}
