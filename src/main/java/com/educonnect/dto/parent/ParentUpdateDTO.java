package com.educonnect.dto.parent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentUpdateDTO {

    /**
     * Optional new name for the parent.
     */
    private String name;

    /**
     * Optional updated contact info.
     */
    private String contactInfo;

    /**
     * Optional new status (e.g., ACTIVE, INACTIVE, SUSPENDED).
     */
    private String status;

    /**
     * Optional: replace/update the set of linked students.
     * If you prefer to manage linking via dedicated endpoints,
     * you can ignore this field in your service implementation.
     */
    private List<UUID> linkedStudentIds;
}