package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.assessment.Assignment;
import com.educonnect.model.assessment.Submission;
import com.educonnect.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, UUID> {
    Optional<Submission> findByStudentAndAssessment(Student student, Assessment assessment);
    boolean existsByStudentAndAssessment(Student student, Assessment assessment);
    @Query(
            """
                   SELECT sub FROM Submission sub
                   JOIN FETCH sub.assignmentAttachmentList aal
                   JOIN FETCH sub.assessment asm
                   JOIN FETCH asm.assignment asn
                   WHERE sub.submissionId = :submissionId  
            """
    )
    Optional<Submission> findAssignmentAndAssessmentAndAttachments(UUID submissionId);
}
