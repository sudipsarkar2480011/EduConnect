package com.educonnect.model.course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class CourseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID moduleId;

    private String title;
    private String contentUrl; // S3 Link or File Path
    private Integer sequenceOrder; // 1, 2, 3...

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}