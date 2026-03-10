package com.educonnect.dto.student;

import com.educonnect.model.course.Enrollment;
import com.educonnect.model.user.Role;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (Record) representing a student's public profile and status.
 * <p>This record provides a snapshot of the student's identity, system role,
 * account activity, and their current course enrollments.</p>
 *
 * @author harini
 * @param userId the unique identifier of the student user.
 * @param fullName the student's complete name.
 * @param email the registered email address of the student.
 * @param role the security role assigned to the user (e.g., STUDENT).
 * @param active indicates if the student account is currently enabled.
 * @param enrollmentList a list of {@link Enrollment} entities associated with the student.
 * @version 1.0
 * @since 1.0
 */
public record StudentResponse(
        UUID userId,
        String fullName,
        String email,
        Role role,
        boolean active,
        List<Enrollment> enrollmentList
) {}