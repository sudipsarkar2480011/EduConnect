package com.educonnect.model.compliance;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "compliance_notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID NoteId;

    private String note;

    @ManyToOne
    @JoinColumn(name = "compliance_record")
    private ComplianceRecord complianceRecord;
}
