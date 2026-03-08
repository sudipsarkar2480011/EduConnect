package com.educonnect.dto.student;

import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Role;

import java.util.List;
import java.util.UUID;

public record StudentResponse(
        UUID userId,
        String fullName,
        String email,
        Role role,
        boolean active,
        List<Enrollment> enrollmentList
) {}