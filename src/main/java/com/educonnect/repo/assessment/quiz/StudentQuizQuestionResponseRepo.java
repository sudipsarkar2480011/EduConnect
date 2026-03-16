package com.educonnect.repo.assessment.quiz;

import com.educonnect.model.assessment.StudentQuizQuestionResponse;
import com.educonnect.model.assessment.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentQuizQuestionResponseRepo extends JpaRepository<StudentQuizQuestionResponse, UUID> {
    List<StudentQuizQuestionResponse> findAllBySubmission(Submission submission);

    @Query(
            """
                 SELECT qr FROM StudentQuizQuestionResponse qr
                 JOIN FETCH qr.question q
                 JOIN FETCH q.questionOptionList ol
                 JOIN FETCH qr.questionOption uo
                 WHERE qr.submission.submissionId = :submissionId 
            """
    )
    List<StudentQuizQuestionResponse> findStudentQuizResponse(@Param("submissionId") UUID submissionId);
}
