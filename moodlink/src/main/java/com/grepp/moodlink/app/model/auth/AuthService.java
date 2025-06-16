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

    public TokenDto signin(String userId, String password){
        // 로그인 시도 시 권한 없이 인증 전 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password);
        // loadUserByUsername + password 검증 후 authentication 반환
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        // 권한이 포함된 토큰 생성
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return processTokenSignin(userId);
    }

    public TokenDto processTokenSignin(String userId) {

        AccessTokenDto dto = jwtProvider.generateAccessToken(userId); // Access토큰 생성
        RefreshToken refreshToken = new RefreshToken(userId, dto.getId()); // Refresh토큰 생성
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
