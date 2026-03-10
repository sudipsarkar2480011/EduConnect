package com.educonnect.dto.parent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request payload to create or update a ParentAccess
 * for a specific student.
 *
 * Fields mirror: ParentAccess(AccessID, ParentID, StudentID, Permissions, Status)
 * - AccessID is not part of the request (server/route supplies it on update).
 * - ParentID is inferred from the URL path in the controller.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentAccessRequestDTO {
    private UUID parentId;
    private UUID studentId;
    private String permissions; // e.g., VIEW_ATTENDANCE, VIEW_PERFORMANCE, ALL
    private String status;      // e.g., ACTIVE, INACTIVE, REVOKED
}