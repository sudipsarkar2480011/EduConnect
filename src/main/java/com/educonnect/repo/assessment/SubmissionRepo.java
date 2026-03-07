package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.assessment.Submission;
import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
    Optional<Submission> findByStudentAndAssessment(Student student, Assessment assessment);
    boolean existsByStudentAndAssessment(Student student, Assessment assessment);
}
