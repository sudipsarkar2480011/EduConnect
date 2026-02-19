package com.educonnect.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "parent_id")
public class Parent extends User {

    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "parent_student_mapping",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> children;
}