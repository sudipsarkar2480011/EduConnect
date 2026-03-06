package com.educonnect.repo.assessment;

import com.educonnect.model.assessment.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentAttachmentRepo extends JpaRepository<AssignmentAttachment,UUID> {
}
