package com.educonnect.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@ToString
public class CourseModule {

    @Id
    @Column(nullable = false,updatable = false)
    private UUID moduleId;

    private String title;
    private String contentUrl; // S3 Link or File Path
    private Integer sequenceOrder; // 1, 2, 3...
    private Double duration ;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;
}