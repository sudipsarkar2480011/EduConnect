package com.educonnect.model.user;

import com.educonnect.model.access.ParentAccess;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "parent_id")
@SuperBuilder
public class Parent extends User {

    private String phoneNumber;

    @OneToMany(mappedBy = "parent")
    private List<Student> children;


    @OneToMany(mappedBy = "parent")
    private List<ParentAccess> parentAccessList;
}