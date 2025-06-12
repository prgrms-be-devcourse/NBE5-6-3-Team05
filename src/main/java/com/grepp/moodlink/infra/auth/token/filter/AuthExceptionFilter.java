package com.grepp.moodlink.infra.auth.token.filter;

import com.grepp.moodlink.infra.error.exceptions.AuthApiException;
import com.grepp.moodlink.infra.error.exceptions.AuthWebException;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
//  인증/인가 관련 예외 처리

//목적: 인증 과정에서 발생하는 예외들을 통합적으로 처리

//처리하는 예외들:
//토큰 만료 (ExpiredJwtException)
//유효하지 않은 토큰 (InvalidTokenException)
//권한 부족 (AccessDeniedException)

//응답: JSON 형태의 에러 응답 반환
//위치: JwtAuthenticationFilter보다 앞에 위치하여 예외를 먼저 캐치
public class AuthExceptionFilter extends OncePerRequestFilter {
    
    private final HandlerExceptionResolver handlerExceptionResolver;
    
    public AuthExceptionFilter(
        @Qualifier("handlerExceptionResolver")
        HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response); // 요청과 응답을 넘겨준다.
        } catch (CommonException ex) {
            throwAuthEx(request, response, ex.code());
        } catch (JwtException ex) {
            throwAuthEx(request, response, ResponseCode.UNAUTHORIZED);
        }
    }
    
    private void throwAuthEx(HttpServletRequest request, HttpServletResponse response,
        ResponseCode code) {

        // /api로 시작할 때 에러 발생시키도록 함
        if (request.getRequestURI().startsWith("/api")) {
            handlerExceptionResolver.resolveException(request, response, null,
                new AuthApiException(code));
            return;
        }

        // 유효하지 않은 토큰이거나 계정에 비정상적인 접근이 발생하면 에러 발생시키도록 함
        if(code.equals(ResponseCode.INVALID_TOKEN) ||
            code.equals(ResponseCode.SECURITY_INCIDENT)
        ){
            handlerExceptionResolver.resolveException(request, response, null,
                new AuthWebException(code, "/member/signin"));
            return;
        }
        
        handlerExceptionResolver.resolveException(request, response, null,
            new AuthWebException(code));
    }
}