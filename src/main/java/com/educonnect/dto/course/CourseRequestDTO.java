package com.educonnect.dto.course;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CourseRequestDTO (
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String courseCode
)
{}
