package com.grepp.moodlink.infra.auth.token;

import com.grepp.moodlink.infra.error.exceptions.AuthApiException;
import com.grepp.moodlink.infra.error.exceptions.AuthWebException;
import com.grepp.moodlink.infra.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationEntryPoint(
        @Qualifier("handlerExceptionResolver")
        HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        ResponseCode responseCode = switch(authException){
            case BadCredentialsException bce -> {
                log.warn(bce.getMessage());
                yield ResponseCode.BAD_CREDENTIAL;
            }
            case CompromisedPasswordException cpe -> {
                log.warn(cpe.getMessage());
                yield ResponseCode.SECURITY_INCIDENT;
            }
            case InsufficientAuthenticationException iae -> {
                log.warn(iae.getMessage());
                yield ResponseCode.UNAUTHORIZED;
            }

            default -> {
                log.warn(authException.getMessage());
                yield ResponseCode.BAD_REQUEST;
            }
        };

        if(request.getRequestURI().startsWith("/api")){
            handlerExceptionResolver.resolveException(request, response, null, new AuthApiException(responseCode));
            return;
        }

        handlerExceptionResolver.resolveException(request, response, null, new AuthWebException(responseCode));
    }
}
