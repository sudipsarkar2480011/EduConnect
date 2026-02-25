package com.educonnect.model.user;


import com.educonnect.model.access.ParentAccess;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.attendance.Attendance;
import com.educonnect.model.compliance.ComplianceRecord;
import com.educonnect.model.course.Enrollment;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.engagement.Engagement;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User {


    private LocalDate dateOfBirth;
    private String enrollmentNumber;

    // Links to Parents
    @ManyToMany(mappedBy = "children")
    private List<Parent> parents;

    // Links to Academic Records (Cross-package relationship)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student")
    private List<StudentDocument> documents;

    @OneToMany(mappedBy = "student")
    private List<ParentAccess> parentAccessList;

    @OneToMany(mappedBy = "student")
    private List<Result> resultList;

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "student")
    private List<ComplianceRecord> complianceRecords;

    @OneToMany(mappedBy = "student")
    private List<Engagement> engagements;


    public void addParent(Parent parent){
        if(this.parents != null){
            this.parents = new ArrayList<>();
        }
        this.parents.add(parent);
        parent.getChildren().add(this);
    }

    public void removeParent(Parent parent){
        if(this.parents != null){
            this.parents.remove(parent);
            parent.getChildren().remove(this);
        }
    }
}
