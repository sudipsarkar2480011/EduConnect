package com.educonnect.model.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionOptionId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String optionText;

    private Boolean isCorrectOption ;

    @OneToMany(mappedBy = "questionOption")
    private List<StudentQuizQuestionResponse> studentQuizQuestionResponseList;
}
