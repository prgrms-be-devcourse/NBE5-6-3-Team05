package com.grepp.moodlink.app.model.auth.token.entity;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter @Setter
@RedisHash(value = "refreshToken", timeToLive = 3600 * 24 * 7)
public class RefreshToken {
    @Id
    private String id = UUID.randomUUID().toString();
    private String userId;
    @Indexed
    private String accessTokenId;
    private String token = UUID.randomUUID().toString();

    public RefreshToken(String userId, String id) {
        this.userId = userId;
        this.accessTokenId = id;
    }
}
