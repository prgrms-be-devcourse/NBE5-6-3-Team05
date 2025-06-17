package com.grepp.moodlink.app.model.auth;

import com.grepp.moodlink.app.model.auth.token.RefreshTokenRepository;
import com.grepp.moodlink.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.moodlink.app.model.auth.token.dto.TokenDto;
import com.grepp.moodlink.app.model.auth.token.entity.RefreshToken;
import com.grepp.moodlink.infra.auth.token.JwtProvider;
import com.grepp.moodlink.infra.auth.token.code.GrantType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    public Authentication signin(String userId, String password){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public TokenDto processTokenSignin(String userId) {

        AccessTokenDto dto = jwtProvider.generateAccessToken(userId);
        RefreshToken refreshToken = new RefreshToken(userId, dto.getId());
        refreshTokenRepository.save(refreshToken);

        return TokenDto.builder()
            .accessToken(dto.getToken())
            .refreshToken(refreshToken.getToken())
            .atExpiresIn(jwtProvider.getAtExpiration())
            .rtExpiresIn(jwtProvider.getRtExpiration())
            .grantType(GrantType.BEARER)
            .build();
    }
}
