package com.educonnect.model.assesment;

import com.educonnect.model.course.Course;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assesment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assesmentId;
    
    private Double maxScore;
    private String title;

    @Enumerated(EnumType.STRING)
    private AssesmentType type;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
