package com.educonnect.model.assessment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionOptionId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String content;

    private boolean isCorrectOption ;
}
