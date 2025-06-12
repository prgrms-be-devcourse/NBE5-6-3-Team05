//package com.grepp.moodlink.infra.auth.token;
//
//import com.grepp.moodlink.infra.error.exceptions.AuthApiException;
//import com.grepp.moodlink.infra.error.exceptions.AuthWebException;
//import com.grepp.moodlink.infra.response.ResponseCode;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InsufficientAuthenticationException;
//import org.springframework.security.authentication.password.CompromisedPasswordException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//// form login : LoginUrlAuthenticationEntryPoint
//// custom EntryPoint 를 등록하면 custom EntryPoint 가 우선적으로 적용
//@Slf4j
//@Component
//
//// 주요 역할: 인증되지 않은 사용자의 접근 처리
//
////    목적: 인증이 필요한 리소스에 인증되지 않은 사용자가 접근할 때의 처리
////    구현: AuthenticationEntryPoint 인터페이스 구현
////    응답: 보통 401 Unauthorized 상태코드와 함께 JSON 에러 응답
////    사용 시점: Spring Security에서 인증이 필요하지만 인증되지 않은 경우
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    private final HandlerExceptionResolver handlerExceptionResolver;
//
//    public JwtAuthenticationEntryPoint(
//        @Qualifier("handlerExceptionResolver")
//        HandlerExceptionResolver handlerExceptionResolver) {
//        this.handlerExceptionResolver = handlerExceptionResolver;
//    }
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//        AuthenticationException authException) throws IOException, ServletException {
//        ResponseCode responseCode = switch(authException){
//            case BadCredentialsException bce -> { // 인증 실패, 잘못된 자격 증명
//                log.warn(bce.getMessage());
//                yield ResponseCode.BAD_CREDENTIAL;
//            }
//            case CompromisedPasswordException cpe -> { // 잘못된 비밀번호
//                log.warn(cpe.getMessage());
//                yield ResponseCode.SECURITY_INCIDENT;
//            }
//            case InsufficientAuthenticationException iae -> { // 자격증명을 충분히 신뢰할 수 없음
//                log.warn(iae.getMessage());
//                yield ResponseCode.UNAUTHORIZED;
//            }
//
//            default -> {
//                log.warn(authException.getMessage());
//                yield ResponseCode.BAD_REQUEST;
//            }
//        };
//
//        if(request.getRequestURI().startsWith("/api")){
//            handlerExceptionResolver.resolveException(request, response, null, new AuthApiException(responseCode));
//            return;
//        }
//
//        handlerExceptionResolver.resolveException(request, response, null, new AuthWebException(responseCode));
//    }
//}
