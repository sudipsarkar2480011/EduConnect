package com.educonnect.model.assessment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignmentId;


    @OneToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment ;


    @OneToMany(mappedBy = "assignment")
    private List<AssignmentAttachment> assignmentAttachmentList;

    private LocalDate dueDate;

    private Integer noOfAssignmentDone ;

}
