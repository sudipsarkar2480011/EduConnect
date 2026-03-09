package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionOptionRepo extends JpaRepository<QuestionOption, UUID> {
}
