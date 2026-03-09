package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import com.educonnect.model.assessment.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentQuizQuestionResponseRepo extends JpaRepository<StudentQuizQuestionResponse, UUID> {
    List<StudentQuizQuestionResponse> findAllBySubmission(Submission submission);
}
