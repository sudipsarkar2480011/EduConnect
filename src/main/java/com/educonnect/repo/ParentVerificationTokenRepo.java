package com.educonnect.repo;

import com.educonnect.model.token.ParentVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link ParentVerificationToken} entities.
 *
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * for parent verification tokens. It also defines a custom query method for
 * retrieving tokens by their unique token string.
 *
 * This repository is primarily used in the parent verification workflow to:
 *     Look up verification tokens when parents attempt to verify their account
 *     Validate token existence and expiry during verification
 *
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

public interface ParentVerificationTokenRepo extends JpaRepository<ParentVerificationToken, UUID> {

    /**
     * Retrieves a {@link ParentVerificationToken} based on its unique token value.
     *
     *This method is commonly used when a parent clicks a verification link
     * containing the token. The token is validated to complete the verification process.
     *
     * @param token the unique verification token string
     * @return an {@link Optional} containing the corresponding {@link ParentVerificationToken},
     *         or empty if the token does not exist
     */

    Optional<ParentVerificationToken> findByToken(String token);
}
