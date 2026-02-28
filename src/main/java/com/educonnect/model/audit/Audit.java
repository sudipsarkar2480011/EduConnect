package com.educonnect.model.audit;

import java.time.LocalDate;
import com.educonnect.model.user.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;
    
    private String scope;
    private String findings;
    private LocalDate date;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "admin_id",nullable = false)
    private Admin admin;

}
