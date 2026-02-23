package com.educonnect.model.course;

import com.educonnect.model.assessment.Assessment;
import com.educonnect.model.attendance.Attendance;
import com.educonnect.model.engagement.Engagement;
import com.educonnect.model.user.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID courseUuid = UUID.randomUUID();

    private String title;
    private String description;
    private String courseCode; // e.g., "CS101"

    // Owning side of the relationship
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseModule> modules;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Assessment> assessments;

    @OneToMany(mappedBy = "course")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "course")
    private List<Engagement> engagements;
}