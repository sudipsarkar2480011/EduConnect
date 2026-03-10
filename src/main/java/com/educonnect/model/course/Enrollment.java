package com.educonnect.model.course;

import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
public class Enrollment {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID enrollmentId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "student_id" )
    private Student student;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private boolean isActive = true;

    private Double remainingDuration;

    private Double progress;

    private Double finalGrade;
}