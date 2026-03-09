package com.educonnect.model.user;

import com.educonnect.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;

        Teacher teacher = (Teacher) o;

        System.out.println("Inside equals -- >> "
                + (this.getUserId() != null && this.getUserId().equals(teacher.getUserId())));

        return this.getUserId() != null && this.getUserId().equals(teacher.getUserId());
    }
}