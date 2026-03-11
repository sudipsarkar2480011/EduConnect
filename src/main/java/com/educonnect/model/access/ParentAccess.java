package com.educonnect.model.access;

import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;


// ParentAccess(AccessID, ParentID, StudentID, Permissions, Status)
@Entity
@Data
public class ParentAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID parentAccessId;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    private String permission;
    private String status;
}
