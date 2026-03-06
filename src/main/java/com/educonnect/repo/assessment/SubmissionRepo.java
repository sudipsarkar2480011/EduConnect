package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
}
