package com.educonnect.utils;

import com.educonnect.model.user.Student;
import com.educonnect.model.user.User;
import com.educonnect.utils.validators.UserValidator;

import java.util.Optional;

public class UserValidation {
    public static final UserValidator<User> EMAIL_VALID = dto ->
            (dto.getEmail() != null && dto.getEmail().contains("@"))
                    ? Optional.empty() : Optional.of("Invalid Email");

    public static final UserValidator<User> PASSWORD_LENGTH = dto ->
            (dto.getPassword() != null && dto.getPassword().length() >= 8)
                    ? Optional.empty() : Optional.of("Password too short");

    public static final UserValidator<User> PASSWORD_STRENGTH = dto -> {
        String password = dto.getPassword();

        if (password == null) {
            return Optional.of("Password is required");
        }

        // Regex for: 4-32 chars, 1 digit, 1 caps, 1 small, 1 special
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{4,32}$";

        return password.matches(passwordRegex)
                ? Optional.empty()
                : Optional.of("Password must be 4-32 characters and include at least " +
                "one uppercase, one lowercase, one digit, and one special character.");
    };

    public static final UserValidator<Student> STUDENT_EXTRA_INFO = dto ->
            (dto.getEnrollmentNumber() != null && !dto.getEnrollmentNumber().isBlank())
                    ? Optional.empty() : Optional.of("Enrollment number missing");
}
