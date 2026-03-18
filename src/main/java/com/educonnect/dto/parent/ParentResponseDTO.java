package com.educonnect.dto.parent;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentResponseDTO {

    /**
     * Parent unique identifier.
     */
    private UUID id;

    /**
     * Parent/Guardian display name.
     */
    private String name;

    /**
     * Free-form contact information (email/phone/address, etc.).
     */
    private String contactInfo;

    /**
     * Parent status (e.g., ACTIVE, INACTIVE, SUSPENDED).
     */
    private Boolean status;

    /**
     * Students linked to this parent.
     */
    private List<UUID> linkedStudentIds;
}