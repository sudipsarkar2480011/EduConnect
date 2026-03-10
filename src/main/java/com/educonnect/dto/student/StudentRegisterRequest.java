package com.educonnect.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (Record) for student registration.
 * <p>Used to capture the essential credentials and identity details
 * required to create a new student account in the system.</p>
 *
 * @author harini
 * @param fullName the complete name of the student (must not be blank).
 * @param email a unique and valid email address used for login and notifications.
 * @param password the plain-text password to be encrypted during the registration process.
 * @version 1.0
 * @since 1.0
 */

public record StudentRegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String password
) {}