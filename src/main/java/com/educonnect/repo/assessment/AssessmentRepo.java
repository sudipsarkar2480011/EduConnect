package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentRepo extends JpaRepository<Assessment, UUID> {
}
