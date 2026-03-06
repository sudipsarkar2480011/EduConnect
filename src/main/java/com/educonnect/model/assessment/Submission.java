package com.educonnect.model.assessment;

import com.educonnect.model.user.Student;
import com.educonnect.model.user.Teacher;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID submissionId;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
