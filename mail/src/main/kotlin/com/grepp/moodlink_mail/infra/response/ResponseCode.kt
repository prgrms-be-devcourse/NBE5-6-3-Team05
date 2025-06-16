package com.grepp.moodlink_mail.infra.response

import org.springframework.http.HttpStatus

enum class ResponseCode(
    private val code: String,
    private val status: HttpStatus,
    private val message: String
) {
    OK("0000", HttpStatus.OK, "정상적으로 완료되었습니다."),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("4010", HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    INVALID_TOKEN("4002", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 입니다."),
    SECURITY_INCIDENT("6000", HttpStatus.UNAUTHORIZED, "계정에 비정상적인 접근이 감지되었습니다.");

    fun code(): String {
        return code
    }

    fun status(): HttpStatus {
        return status
    }

    fun message(): String {
        return message
    }
}
