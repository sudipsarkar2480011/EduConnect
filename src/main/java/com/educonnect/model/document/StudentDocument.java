package com.educonnect.model.document;

import com.educonnect.model.user.Student;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


//StudentDocument(DocumentID, StudentID, DocType
//[IDProof/Transcript], FileURI, UploadedDate, VerificationStatus)
//StudentDocument(DocumentID, StudentID, DocType, FileURI, UploadedDate,
//VerificationStatus)

@Entity
@Data
public class StudentDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID documentUuid = UUID.randomUUID();

    @Column(nullable = false)
    private String fileName ;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    private DocType docType;


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] fileData;


    @CreationTimestamp
    private LocalDateTime UploadedDate;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;


}
