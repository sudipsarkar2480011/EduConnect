package com.educonnect.model.assessment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID questionId;

    @OneToMany(mappedBy = "question")
    private List<QuestionOption> questionOptionList;


}
