package com.educonnect.model.engagement;

//EngagementID, StudentID, CourseID, Activity, Timestamp

import com.educonnect.model.course.Course;
import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID engagementId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    private String activityDescription;

    @CreationTimestamp
    private LocalDateTime timestamp;


}
