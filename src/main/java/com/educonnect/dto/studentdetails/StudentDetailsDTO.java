package com.educonnect.dto.studentdetails;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Builder
@Data
public class StudentDetailsDTO {

    private UUID studentUuid;
    private LocalDate dateOfBirth;
    private String enrollmentNumber;
    private UUID parentId;
    private List<UUID> enrollmentIds;
    private List<UUID> documentIds;

}
