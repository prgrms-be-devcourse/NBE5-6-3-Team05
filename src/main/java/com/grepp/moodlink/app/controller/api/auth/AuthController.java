package com.grepp.moodlink.app.controller.api.auth;

import com.grepp.moodlink.app.controller.api.auth.payload.SigninRequest;
import com.grepp.moodlink.app.controller.api.auth.payload.TokenResponse;
import com.grepp.moodlink.app.model.auth.AuthService;
import com.grepp.moodlink.app.model.auth.token.RefreshTokenService;
import com.grepp.moodlink.app.model.auth.token.dto.TokenDto;
import com.grepp.moodlink.infra.auth.token.JwtProvider;
import com.grepp.moodlink.infra.auth.token.TokenCookieFactory;
import com.grepp.moodlink.infra.auth.token.code.GrantType;
import com.grepp.moodlink.infra.auth.token.code.TokenType;
import com.grepp.moodlink.infra.response.ApiResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("signin")
    public ResponseEntity<ApiResponse<TokenResponse>> signin(
        @RequestBody
        SigninRequest req,
        HttpServletResponse response
    ) {
        TokenDto dto = authService.signin(req.getUserId(), req.getPassword());
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(),
            dto.getAccessToken(), dto.getAtExpiresIn());
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(),
            dto.getRefreshToken(), dto.getRtExpiresIn());
        
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        TokenResponse tokenResponse = TokenResponse.builder()
                                          .accessToken(dto.getAccessToken())
                                          .expiresIn(dto.getAtExpiresIn())
                                          .grantType(GrantType.BEARER)
                                          .build();
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }
    
    @PostMapping("logout")
    public void logout(
        HttpServletResponse response,
        HttpServletRequest request
    ){
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (accessToken != null) {
            Claims claims = jwtProvider.parseClaim(accessToken);
            refreshTokenService.deleteByAccessTokenId(claims.getId());
        }

        System.out.println("qwer");

        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.ACCESS_TOKEN).toString());
        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.REFRESH_TOKEN).toString());
        response.addHeader("Set-Cookie", TokenCookieFactory.createExpiredToken(TokenType.AUTH_SERVER_SESSION_ID).toString());
        System.out.println("asdasda");

    }

}
