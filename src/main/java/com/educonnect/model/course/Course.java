package com.educonnect.model.course;

import com.educonnect.model.user.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

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
}