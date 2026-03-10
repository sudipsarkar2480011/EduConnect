package com.educonnect.model.compliance;

//ComplianceRecord(ComplianceID, StudentID, Type, Result, Date, Notes)

import com.educonnect.model.user.Student;
import com.educonnect.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CurrentTimestamp;


import java.time.LocalDate;
import java.util.ArrayList;
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
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ComplianceType type;

    private String result;

    @CurrentTimestamp
    private LocalDate date;

    @OneToMany(mappedBy = "complianceRecord")
    @ToString.Exclude // Prevents infinite loop in logging/debugging
    @EqualsAndHashCode.Exclude // Prevents infinite loop in collections
    private List<Note> notes = new ArrayList<>();


}
