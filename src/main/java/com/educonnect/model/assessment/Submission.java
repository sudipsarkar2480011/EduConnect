package com.educonnect.model.assessment;

import com.educonnect.model.user.Student;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_submission",
                        columnNames = {"assessment_id","student_id"}
                )
        }
)
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID submissionId;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;


    @OneToMany(mappedBy = "submission")
    private List<AssignmentAttachment> assignmentAttachmentList;


    @OneToMany(mappedBy = "submission")
    private List<StudentQuizQuestionResponse> studentQuizQuestionResponseList;


}
