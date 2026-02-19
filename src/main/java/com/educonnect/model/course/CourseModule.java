package com.educonnect.model.course;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    private String title;
    private String contentUrl; // S3 Link or File Path
    private Integer sequenceOrder; // 1, 2, 3...

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}