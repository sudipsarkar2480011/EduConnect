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
public class ParentCreateDTO {

    /**
     * Parent/Guardian display name.
     * (Mapped to base User.name in your model hierarchy)
     */
    private String name;

    /**
     * Free-form contact information (email/phone/address, etc.).
     */
    private String contactInfo;

    /**
     * Parent status (e.g., ACTIVE, INACTIVE, SUSPENDED).
     */
    private String status;

    /**
     * Optional: initial list of student IDs to link with this parent.
     * You can also use the dedicated link endpoint later.
     */
    private List<UUID> linkedStudentIds;
}