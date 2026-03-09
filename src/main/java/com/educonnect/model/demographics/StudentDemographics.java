package com.educonnect.model.demographics;

import com.educonnect.model.demographics.embeddables.Address;
import com.educonnect.model.demographics.embeddables.EmergencyContact;
import com.educonnect.model.demographics.enums.BloodGroup;
import com.educonnect.model.demographics.enums.Gender;
import com.educonnect.model.demographics.enums.VisaType;
import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entity to hold all secondary demographic details of a Student.
 * Shares the Primary Key (UUID) with the Student entity.
 */
@Entity
@Table(name = "student_demographics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDemographics {

    @Id
    private UUID studentId; // Will map to the Student's UUID

    @OneToOne
    @MapsId // Crucial: Tells Hibernate to share the PK with Student
    @JoinColumn(name = "student_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;

    // Legal & Identity
    private String legalFullName; // In case it differs from preferred name
    private String nationality;
    private String religion;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String passportNumber;

    @Enumerated(EnumType.STRING)
    private VisaType visaType;

    // Address Mapping
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "perm_street")),
            @AttributeOverride(name = "city", column = @Column(name = "perm_city")),
            @AttributeOverride(name = "state", column = @Column(name = "perm_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "perm_zip")),
            @AttributeOverride(name = "country", column = @Column(name = "perm_country"))
    })
    private Address permanentAddress;

    // Parent Contact info (Directly on demographic to avoid messing with Parent entity right now)
    private String parentEmail;
    private String parentPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "contactName", column = @Column(name = "emerg_contact_name")),
            @AttributeOverride(name = "relationship", column = @Column(name = "emerg_relationship")),
            @AttributeOverride(name = "contactPhone", column = @Column(name = "emerg_phone"))
    })
    private EmergencyContact emergencyContact;
}