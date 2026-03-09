package com.educonnect.model.assessment;

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
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID quizId;

    @OneToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment ;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questionList;


    @OneToMany(mappedBy = "quiz")
    private List<StudentQuizQuestionResponse> studentQuizResponseList;
}
