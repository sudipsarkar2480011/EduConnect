package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, UUID> {
}
