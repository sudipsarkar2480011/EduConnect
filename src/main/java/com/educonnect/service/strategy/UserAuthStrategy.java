package com.educonnect.service.strategy;

import com.educonnect.config.JWTService;
import com.educonnect.config.UserRepo;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.model.user.User;
<<<<<<< Updated upstream
=======
import com.educonnect.service.contract.RefreshTokenService;
>>>>>>> Stashed changes
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    default LoginResponseDTO verify(User u, AuthenticationManager authManager, JWTService jwtService, UserRepo userRepo) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User entity = userRepo.findByEmail(u.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found after auth"));
<<<<<<< Updated upstream
            String token = jwtService.generateToken(entity.getEmail());
            return LoginResponseDTO.builder().token(token).name(entity.getFullName())
                    .role(String.valueOf(entity.getRole())).email(entity.getEmail())
=======
            String accessToken = jwtService.generateToken(entity.getEmail());
            String refreshToken = refreshTokenService.createToken(entity.getUserId())
                    .getToken();

            return UserResponseDTO.builder()
                    .uuid(entity.getUserId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .name(entity.getFullName())
                    .role(String.valueOf(entity.getRole()))
                    .email(entity.getEmail())
>>>>>>> Stashed changes
                    .build();
        }
        throw new RuntimeException("Authentication Failed");
    }
}
