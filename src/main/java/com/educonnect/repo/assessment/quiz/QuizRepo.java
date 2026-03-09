package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepo extends JpaRepository<Quiz, UUID> {
}
