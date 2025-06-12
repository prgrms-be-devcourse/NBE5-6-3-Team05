package com.grepp.moodlink.infra.auth.token.filter;

import com.grepp.moodlink.app.model.auth.token.RefreshTokenService;
import com.grepp.moodlink.app.model.auth.token.dto.AccessTokenDto;
import com.grepp.moodlink.app.model.auth.token.entity.RefreshToken;
import com.grepp.moodlink.infra.auth.token.JwtProvider;
import com.grepp.moodlink.infra.auth.token.TokenCookieFactory;
import com.grepp.moodlink.infra.auth.token.code.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
// JWT 토큰 검증 및 인증 처리. HTTP 요청에서 JWT 토큰을 추출하고 검증하여 사용자 인증을 처리

// 동작 과정:
// 1. 요청 헤더(Authorization) 또는 쿠키에서 JWT 토큰 추출
// 2. 토큰 유효성 검증 (만료시간, 서명 등)
// 3. 토큰에서 사용자 정보 추출
// 4. SecurityContext에 Authentication 객체 설정

// 위치: Spring Security 필터 체인의 앞쪽에 위치
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
//    private final UserBlackListRepository userBlackListRepository;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludePath = new ArrayList<>();
        excludePath.addAll(List.of("/error", "/favicon.ico", "/css", "/img","/js","/download"));
        excludePath.addAll(List.of("/member/signup", "/member/signin"));
        String path = request.getRequestURI();
        return excludePath.stream().anyMatch(path::startsWith);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        
        log.info(request.getRequestURI());
        
        String requestAccessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (requestAccessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        Claims claims = jwtProvider.parseClaim(requestAccessToken);
//        if(userBlackListRepository.existsById(claims.getSubject())){
//            filterChain.doFilter(request, response);
//            return;
//        }
        
        try {
            if(jwtProvider.validateToken(requestAccessToken)){
                Authentication authentication = jwtProvider.genreateAuthentication(requestAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            AccessTokenDto newAccessToken = renewingAccessToken(requestAccessToken, request);
            if (newAccessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            
            RefreshToken newRefreshToken = renewingRefreshToken(claims.getId(), newAccessToken.getId());
            responseToken(response, newAccessToken, newRefreshToken);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private void responseToken(HttpServletResponse response, AccessTokenDto newAccessToken,
        RefreshToken newRefreshToken) {
        
        ResponseCookie accessTokenCookie =
            TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(), newAccessToken.getToken(),
                3000000L);
        
        ResponseCookie refreshTokenCookie =
            TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(), newRefreshToken.getToken(),
                jwtProvider.getAtExpiration());
        
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
    
    private RefreshToken renewingRefreshToken(String id, String newTokenId) {
        return refreshTokenService.renewingToken(id, newTokenId);
    }
    
    private AccessTokenDto renewingAccessToken(String requestAccessToken, HttpServletRequest request) {
        Authentication authentication = jwtProvider.genreateAuthentication(requestAccessToken);
        String refreshToken = jwtProvider.resolveToken(request, TokenType.REFRESH_TOKEN);
        Claims claims = jwtProvider.parseClaim(requestAccessToken);
        
        RefreshToken storedRefreshToken = refreshTokenService.findByAccessTokenId(claims.getId());
        
        if(storedRefreshToken == null) {
            return null;
        }
        
//        if (!storedRefreshToken.getToken().equals(refreshToken)) {
//            userBlackListRepository.save(new UserBlackList(authentication.getName()));
//            throw new CommonException(ResponseCode.SECURITY_INCIDENT);
//        }
        
        return generateAccessToken(authentication);
    }
    
    private AccessTokenDto generateAccessToken(Authentication authentication) {
        AccessTokenDto newAccessToken = jwtProvider.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return newAccessToken;
    }
}
