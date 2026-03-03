package com.educonnect.service.implementation;

import com.educonnect.config.UserRepo;
import com.educonnect.model.token.RefreshToken;
import com.educonnect.repo.RefreshTokenRepo;
import com.educonnect.service.contract.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final Long refreshTokenDurationMS=86400L; //1day

    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshToken createToken(UUID userId) throws Exception {
        return  refreshTokenRepo.save(
                RefreshToken.builder()
                        .user(userRepo.findById(userId).orElseThrow(
                                ()->new Exception("User not found")
                        ))
                        .expiryDate(Instant.now().plusMillis(refreshTokenDurationMS))
                        .token(UUID.randomUUID().toString())
                        .build()
        );
    }

    public RefreshToken verifyToken(RefreshToken token) throws Exception {
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.delete(token);
            throw new Exception("Invalid token");
        }
        return  token;
    }

    @Override
    public Optional<RefreshToken> findByToken(RefreshToken token) throws Exception {
        return refreshTokenRepo.findByToken(token.getToken());
    }

}
