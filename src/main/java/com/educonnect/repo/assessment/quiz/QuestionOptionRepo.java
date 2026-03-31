package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionOptionRepo extends JpaRepository<QuestionOption, UUID> {
    List<QuestionOption> findAllByQuestionQuestionId(UUID questionId);
}
