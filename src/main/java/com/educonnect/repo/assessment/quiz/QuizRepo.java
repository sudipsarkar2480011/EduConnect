package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, UUID> {
}
