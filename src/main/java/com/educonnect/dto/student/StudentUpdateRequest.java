package com.educonnect.dto.student;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating existing student information.
 * <p>Uses Lombok annotations to generate boilerplate code for data handling.
 * Fields are optional and used to patch existing records.</p>
 *
 * @author harini
 * @version 1.0
 * @since 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    /** * The updated full name of the student.
     * If provided, this will overwrite the existing name in the database.
     */
    private String fullName;

    /** * The updated email address for the student.
     * Must be a syntactically valid email format.
     */
    @Email
    private String email;

    /** * The account status flag.
     * Set to {@code true} to enable the student or {@code false} to deactivate.
     */
    private Boolean active;
}