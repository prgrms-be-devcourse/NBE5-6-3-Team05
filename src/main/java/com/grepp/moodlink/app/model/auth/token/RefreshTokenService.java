package com.grepp.moodlink.app.model.auth.token;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.grepp.moodlink.app.model.auth.token.entity.RefreshToken;


@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    
    public void deleteByAccessTokenId(String id) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByAccessTokenId(id);
        optional.ifPresent(e -> refreshTokenRepository.deleteById(e.getId()));
    }
    
    public RefreshToken renewingToken(String id, String newTokenId) {
        RefreshToken refreshToken = findByAccessTokenId(id);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAccessTokenId(newTokenId);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    
    public RefreshToken findByAccessTokenId(String id){
        return refreshTokenRepository.findByAccessTokenId(id).orElse(null);
    }
}
