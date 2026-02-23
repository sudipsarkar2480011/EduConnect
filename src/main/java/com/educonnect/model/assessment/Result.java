package com.educonnect.model.assessment;

import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

//ResultID, AssessmentID, StudentID, Score, Status

@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    private UUID resultUuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private int score ;

    @Enumerated(EnumType.STRING)
    private ResultStatus status;

}
