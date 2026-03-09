package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepo extends JpaRepository<Question, UUID> {
}
