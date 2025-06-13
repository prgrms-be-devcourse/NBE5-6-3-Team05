package com.grepp.moodlink.infra.auth.token.filter;

import com.grepp.moodlink.app.model.auth.token.RefreshTokenService;
import com.grepp.moodlink.infra.auth.token.JwtProvider;
import com.grepp.moodlink.infra.auth.token.TokenCookieFactory;
import com.grepp.moodlink.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor

//주요 역할: 로그아웃 처리

//목적: 사용자 로그아웃 요청을 처리

//동작:
// 1. 로그아웃 URL 패턴 매칭
// 2. 토큰 무효화 (블랙리스트 추가 또는 DB에서 삭제)
// 3. 쿠키 삭제
// 4. SecurityContext 정리

//응답: 로그아웃 성공 응답 또는 리다이렉트
public class LogoutFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);

        if(accessToken == null){
            filterChain.doFilter(request,response);
            return;
        }

        String path = request.getRequestURI();
        Claims claims  = jwtProvider.parseClaim(accessToken);

        if(path.equals("/logout")){
            refreshTokenService.deleteByAccessTokenId(claims.getId());
            ResponseCookie expiredAccessToken = TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN);
            ResponseCookie expiredRefreshToken = TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN);
            ResponseCookie expiredSessionId = TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID);
            response.addHeader("Set-Cookie", expiredAccessToken.toString());
            response.addHeader("Set-Cookie", expiredRefreshToken.toString());
            response.addHeader("Set-Cookie", expiredSessionId.toString());
            response.sendRedirect("/");
            return;
        }

        filterChain.doFilter(request,response);
    }
}
