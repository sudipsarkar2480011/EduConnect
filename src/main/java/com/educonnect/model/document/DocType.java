package com.educonnect.model.document;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID docTypeUuid ;
            //= UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private DocTypeEnum docTypeName;



    private String description;

    @OneToMany(mappedBy = "docType")
    List<StudentDocument> studentDocumentList;

}
