package com.educonnect.service.strategy;

import com.educonnect.model.user.User;

/**
 * Strategy interface for handling user authentication and persistence
 * based on specific user roles (e.g., STUDENT, TEACHER, ADMIN).
 * <p>
 * Implementing classes define how a specific role is identified and
 * how that user's data is uniquely persisted to the data store.
 */
public interface UserAuthStrategy {

    /**
     * Determines if this strategy implementation can handle the given user role.
     *
     * @param role A string representation of the user's role.
     * @return {@code true} if this strategy supports the role; {@code false} otherwise.
     */
    boolean supports(String role);

    /**
     * Executes the business logic to persist a user to the database.
     * <p>
     * This method is typically called after the strategy has been
     * identified via {@link #supports(String)}.
     *
     * @param u The user entity to be saved.
     * @return The persisted {@link User} object, often including a generated ID.
     */
    User save(User u);
}
