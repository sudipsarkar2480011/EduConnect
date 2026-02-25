package com.educonnect.model.report;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;


//Report(ReportID, Scope, Metrics, GeneratedDate)

@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID reportId;

    private String scope;
    private String metrics;

    @CurrentTimestamp
    private LocalDate date;


}
