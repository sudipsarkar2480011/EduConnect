package com.educonnect.repo.attachment;

import com.educonnect.model.assessment.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentAttachmentRepo extends JpaRepository<AssignmentAttachment, UUID> {
    List<AssignmentAttachment> findAllBySubmissionSubmissionId(UUID submissionId);
}
