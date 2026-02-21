package com.educonnect.model.audit;

import java.time.LocalDate;
import java.util.UUID;

import com.educonnect.model.user.Admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID auditUuid;
    
    private String scope;
    private String findings;

    @CreationTimestamp
    private LocalDate date;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "admin_id",nullable = false)
    private Admin admin;

}
