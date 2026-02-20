package com.educonnect.model.assesment;

import com.educonnect.model.course.Course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assesment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assesmentId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID assesmentUuid = UUID.randomUUID();
    
    private Double maxScore;
    private String title;

    @Enumerated(EnumType.STRING)
    private AssesmentType type;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
