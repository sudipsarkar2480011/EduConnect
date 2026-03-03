package com.educonnect.service.contract;

import com.educonnect.model.token.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
     RefreshToken createToken(UUID userId) throws Exception;
    RefreshToken verifyToken(RefreshToken token) throws Exception;
    Optional<RefreshToken> findByToken(RefreshToken token) throws Exception;
}
