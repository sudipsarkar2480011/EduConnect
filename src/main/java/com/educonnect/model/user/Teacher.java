package com.educonnect.model.user;

import com.educonnect.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@SuperBuilder
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "teacher_id")
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {

    private String department;
    private String qualification;

    // Cross-package relationship to Course
    @OneToMany(mappedBy = "teacher")
    private List<Course> coursesTaught;

}