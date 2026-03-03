package com.educonnect.dto.parent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response payload representing a ParentAccess record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentAccessResponseDTO {

    private UUID accessId;
    private UUID parentId;
    private UUID studentId;
    private String permissions; // e.g., VIEW_ATTENDANCE, VIEW_PERFORMANCE, ALL
    private String status;      // e.g., ACTIVE, INACTIVE, REVOKED
}