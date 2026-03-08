package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentQuizQuestionResponseRepo extends JpaRepository<StudentQuizQuestionResponse, UUID> {
}
