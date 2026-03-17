package com.educonnect.model.user;


import com.educonnect.model.access.ParentAccess;
import com.educonnect.model.assessment.Result;
import com.educonnect.model.assessment.Submission;
import com.educonnect.model.attendance.Attendance;
import com.educonnect.model.compliance.ComplianceRecord;
import com.educonnect.model.course.Enrollment;
import com.educonnect.model.document.StudentDocument;
import com.educonnect.model.engagement.Engagement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@SuperBuilder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true,exclude = "enrollments")
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User {


    private LocalDate dateOfBirth;
    private String enrollmentNumber;
    private String parentEmail;

    // Links to Parent
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

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

    @OneToMany(mappedBy = "user")
    private List<ComplianceRecord> complianceRecords;


    @OneToMany(mappedBy = "student")
    private List<Engagement> engagements;


    @OneToMany(mappedBy = "student")
    private List<Submission> submissionList;


}
