package com.educonnect.dto.student;

import jakarta.validation.constraints.Email;

public record StudentUpdateRequest(
        String fullName,
        @Email String email,
        Boolean active
) {}