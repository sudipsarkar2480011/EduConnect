package com.educonnect.model.assessment;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class StudentQuizResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentQuizResponseId;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    private Integer score;

    private QuizStatus quizStatus;
}
