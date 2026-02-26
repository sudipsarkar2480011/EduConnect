package com.educonnect.model.user;

import com.educonnect.model.access.ParentAccess;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "parent_id")
public class Parent extends User {

    private String phoneNumber;

    @OneToMany(mappedBy = "parent")
    private List<Student> children;


    @OneToMany(mappedBy = "parent")
    private List<ParentAccess> parentAccessList;
}