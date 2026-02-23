package com.educonnect.model.access;

import com.educonnect.model.user.Parent;
import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


// ParentAccess(AccessID, ParentID, StudentID, Permissions, Status)
@Entity
@Data
public class ParentAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentAccessId;

    private UUID parentAccessUuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    private String permission;
    private String status;
}
