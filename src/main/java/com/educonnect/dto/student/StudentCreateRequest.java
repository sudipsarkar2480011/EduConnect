package com.educonnect.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Data Transfer Object (Record) for creating a new student profile.
 * <p>This record ensures that all required student identification and
 * contact information is captured during the initial creation process.</p>
 *
 * @author harini
 * @param userId the unique identifier linked to the student's User account.
 * @param firstName the student's first name (must not be blank).
 * @param lastName the student's last name (must not be blank).
 * @param email a valid email address for student communication.
 * @version 1.0
 * @since 1.0
 */

public record StudentCreateRequest(
        @NotNull UUID userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email
) {}