package com.educonnect.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class StudentDetailsDTO {

    private UUID studentUuid;
    private LocalDate dateOfBirth;
    private String enrollmentNumber;
    private List<UUID> parentUuids;
    private List<UUID> enrollmentUuids;
    private List<UUID> documentUuids;

}
