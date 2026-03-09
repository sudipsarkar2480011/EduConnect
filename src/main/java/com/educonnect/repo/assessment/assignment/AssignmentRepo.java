package com.educonnect.repo.assessment.assignment;

import com.educonnect.model.assessment.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepo extends JpaRepository<Assignment, UUID> {
}
