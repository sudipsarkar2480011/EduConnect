package com.educonnect.model.assessment;

import com.educonnect.model.course.Course;

import com.educonnect.model.user.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID assessmentId;

    private Double maxScore;
    private String title;

    @Enumerated(EnumType.STRING)
    private AssessmentType type;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(mappedBy = "assessment")
    private Assignment assignment;

    private Integer noOfStudentSubmitted;

    @OneToOne(mappedBy = "assessment")
    private Quiz quiz;

    @OneToMany(mappedBy = "assessment")
    private List<Submission> submissionList;

    @OneToMany(mappedBy = "assessment")
    private List<Result> resultList;

}
