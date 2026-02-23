package com.educonnect.model.compliance;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "compliance_notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NoteId;

    private UUID noteUuid = UUID.randomUUID();

    private String note;

    @ManyToOne
    @JoinColumn(name = "compliance_record")
    private ComplianceRecord complianceRecord;
}
