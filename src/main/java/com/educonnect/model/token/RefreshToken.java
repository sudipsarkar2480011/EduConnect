package com.educonnect.model.token;

import com.educonnect.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;
}
