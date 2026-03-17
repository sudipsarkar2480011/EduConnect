package com.educonnect.model.token;

import com.educonnect.model.user.Parent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a verification token associated with a parent account.
 * This token is typically generated during parent registration and is used to
 * verify the parent's email address or identity. The token is unique and has a defined
 * expiry time, after which it becomes invalid.
 * The token is linked to a specific parent through a one-to-one relationship.
 *     token:a unique string used for verification
 *     expiryDate:the date and time after which the token is invalid
 *     parent:the parent account associated with this token
 * @author abramya975
 * @version 1.0
 * @since 1.0
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="parent_verification_token")
public class ParentVerificationToken {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String token;
    private LocalDateTime expiryDate;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
