package com.grepp.moodlink.infra.auth.token.code;

public enum GrantType {
    BEARER,  // JWT 토큰을 Authorization: Bearer {token} 형태로 전송
    BASIC,   // Basic 인증 (username:password를 Base64 인코딩)
    DIGEST,  // Digest 인증 (해시 기반 인증)
    HOBA,    // HTTP Origin-Bound Authentication
    MUTUAL;  // 상호 인증
}
