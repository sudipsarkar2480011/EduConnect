package com.educonnect.service.implementation;

import com.educonnect.config.UserRepo;
import com.educonnect.model.token.RefreshToken;
import com.educonnect.repo.RefreshTokenRepo;
import com.educonnect.service.contract.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final Long refreshTokenDurationMS=86400*1000L; //1day
    private final SecureRandom random = new SecureRandom();
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    @Transactional
    public RefreshToken createToken(UUID userId) throws Exception {

        Optional<RefreshToken> optionalToken = refreshTokenRepo.findByUserUserId(userId);
        if(optionalToken.isPresent()){
           RefreshToken token = optionalToken.get();
           token.setToken(generateToken());
           token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMS));
           refreshTokenRepo.save(token);
           return  token;
        }
        return  refreshTokenRepo.save(
                RefreshToken.builder()
                        .user(userRepo.findById(userId).orElseThrow(
                                ()->new Exception("User not found")
                        ))
                        .expiryDate(Instant.now().plusMillis(refreshTokenDurationMS))
                        .token(generateToken())
                        .build()
        );
    }

    private String generateToken(){
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }

    @Transactional
    public RefreshToken verifyToken(RefreshToken token) throws Exception {
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepo.deleteByToken(token.getToken());
            throw new Exception("Invalid token");
        }
        return  token;
    }

    @Override
    public Optional<RefreshToken> findByToken(RefreshToken token) throws Exception {
        return refreshTokenRepo.findByToken(token.getToken());
    }

    @Override
    public void deleteToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

}
