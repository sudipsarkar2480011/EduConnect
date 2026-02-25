package com.educonnect.model.user;

import com.educonnect.model.access.ParentAccess;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
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


    @OneToMany(mappedBy = "parent")
    private List<ParentAccess> parentAccessList;
}