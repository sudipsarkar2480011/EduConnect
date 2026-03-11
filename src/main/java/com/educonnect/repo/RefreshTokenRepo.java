package com.educonnect.repo;

import com.educonnect.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserUserId(UUID userId);
    void deleteByToken(String token);
    Optional<RefreshToken> findByUserUserId(UUID userId);
}
