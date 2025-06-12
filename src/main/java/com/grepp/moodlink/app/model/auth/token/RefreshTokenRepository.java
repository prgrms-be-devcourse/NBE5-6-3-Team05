package com.grepp.moodlink.app.model.auth.token;

import com.grepp.moodlink.app.model.auth.token.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccessTokenId(String id);
    void deleteByToken(String token);
}
