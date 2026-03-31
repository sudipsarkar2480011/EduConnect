package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, UUID> {
    Optional<Quiz> findByAssessmentAssessmentId(UUID assessmentId);


    @Query(
            """
                    SELECT q FROM Quiz q
                    JOIN FETCH q.questionList qq
                    JOIN FETCH qq.questionOptionList
                    WHERE q.assessment.assessmentId = :assessmentId
            """
    )
    Optional<Quiz> findQuizWithQuestionAndOptions(@Param("assessmentId") UUID assessmentId);
}