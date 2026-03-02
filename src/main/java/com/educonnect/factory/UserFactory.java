package com.educonnect.factory;

import com.educonnect.config.JWTService;
import com.educonnect.config.UserRepo;
import com.educonnect.dto.LoginRequestDTO;
import com.educonnect.dto.LoginResponseDTO;
import com.educonnect.model.user.User;
import com.educonnect.service.strategy.UserAuthStrategy;
import com.educonnect.utils.UserValidation;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory class responsible for validating and persisting users based on their roles.
 * <p>
 * This factory utilizes a strategy pattern by maintaining a list of {@link UserAuthStrategy}
 * implementations. It selects the appropriate strategy at runtime based on the
 * {@code User} role.
 * * @author YourName
 */

@Component
public class UserFactory
{
    private final List<UserAuthStrategy> strategyList;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final UserRepo userRepo;
    /**
     * Constructs a new UserFactory with the provided list of strategies.
     * Spring automatically injects all beans implementing {@link UserAuthStrategy}.
     *
     * @param strategyList A list of all available user authentication strategies.
     */
    public  UserFactory(List<UserAuthStrategy>  strategyList, AuthenticationManager authManager, JWTService jwtService,UserRepo userRepo){
        this.strategyList=strategyList;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepo=userRepo;
    }



    /**
     * Validates a user's credentials and saves them using the appropriate role-based strategy.
     * <p>
     * The validation pipeline checks:
     * <ul>
     * <li>Email format validity</li>
     * <li>Minimum password length</li>
     * <li>Overall password strength</li>
     * </ul>
     *
     * @param u The user object containing registration details.
     * @return The persisted {@link User} object.
     * @throws RuntimeException If the user fails validation or if no strategy is found for the user's role.
     */
    public User executeSave(User u){
        UserValidation.EMAIL_VALID
                .and(UserValidation.PASSWORD_LENGTH)
                .and(UserValidation.PASSWORD_STRENGTH)
                .validateOrThrow(u);

        return  strategyList.stream()
                .filter(s->s.supports(u.getRole().toString()))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Role not supported"))
                .save(u);
    }
    /**
     * <p>
     * This method executes the following pipeline:
     * <ul>
     * <li>Maps the request data to a transient {@link User} entity.</li>
     * <li>Delegates authentication and token generation </li>
     * </ul>
     *
     * @param requestDTO Data transfer object containing the user's email, password, and role.
     * @return A {@link LoginResponseDTO} containing the generated JWT and user metadata.
     */
    public LoginResponseDTO verify(LoginRequestDTO requestDTO) {
      return strategyList.stream()
                .filter(s -> s.supports(requestDTO.getRole()))
                .findFirst()
                .map(s -> {
                    User u = new User();
                    u.setEmail(requestDTO.getEmail());
                    u.setPassword(requestDTO.getPassword());
                    return s.verify(u, authManager, jwtService, userRepo);
                })
                .orElseThrow(() -> new RuntimeException("Unsupported Role"));
    }
}
