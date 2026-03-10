package com.educonnect.model.document;

import com.educonnect.model.user.Student;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;


//StudentDocument(DocumentID, StudentID, DocType
//[IDProof/Transcript], FileURI, UploadedDate, VerificationStatus)
//StudentDocument(DocumentID, StudentID, DocType, FileURI, UploadedDate,
//VerificationStatus)

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDocument {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false,updatable = false)
    private UUID studentDocumentId;

    @Column(nullable = false)
    private String fileName ;

    @Column(nullable = false,updatable = false)
    private String fileUri;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "doctype_id" , nullable = false)
    private DocType docType;

    @Enumerated(EnumType.STRING)
    private FileTypeEnum fileType;


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileData;


    @CreationTimestamp
    private LocalDateTime UploadedDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;


}
