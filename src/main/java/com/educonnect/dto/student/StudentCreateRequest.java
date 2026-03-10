package com.educonnect.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentCreateRequest(
        @NotNull UUID userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @Email String parentEmail
) {}