package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepo extends JpaRepository<Question, UUID> {
}
