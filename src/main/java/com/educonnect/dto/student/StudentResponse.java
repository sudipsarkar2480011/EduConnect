package com.educonnect.dto.student;

import com.educonnect.model.user.Role;

import java.util.UUID;

public record StudentResponse(
        UUID userId,
        String fullName,
        String email,
        Role role,
        boolean active
) {}