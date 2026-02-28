package com.educonnect.model.compliance;

//ComplianceRecord(ComplianceID, StudentID, Type, Result, Date, Notes)

import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class ComplianceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID complianceRecordID;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private ComplianceType type;

    private String result;

    @CurrentTimestamp
    private LocalDate date;

    @OneToMany(mappedBy = "complianceRecord")
    private List<Note> notes;


}
