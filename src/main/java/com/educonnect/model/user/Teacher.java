package com.educonnect.model.user;

import com.educonnect.model.course.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "teacher_id")
public class Teacher extends User {

    private String department;
    private String qualification;

    // Cross-package relationship to Course
    @OneToMany(mappedBy = "teacher")
    private List<Course> coursesTaught;
}