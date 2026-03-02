package com.educonnect.service.dto;

import jakarta.validation.constraints.Email;

public record StudentUpdateRequest(
        String fullName,
        @Email String email,
        Boolean active
) {}