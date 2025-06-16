package com.grepp.moodlink.infra.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK("0000", HttpStatus.OK, "정상적으로 완료되었습니다."),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("4001", HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    DUPLICATED_DATA("4002", HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    USER_NOTFOUND("4003", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 입니다."),
    EXTERNAL_API_TIMEOUT("5101", HttpStatus.GATEWAY_TIMEOUT, "외부 API 요청이 타임아웃되었습니다."),
    EXTERNAL_API_UNAVAILABLE("5001", HttpStatus.SERVICE_UNAVAILABLE, "외부 API에 연결할 수 없습니다."),
    RESOURCE_EXHAUSTED("5002", HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다."),
    INVALID_TOKEN("4003", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    SECURITY_INCIDENT("6000", HttpStatus.UNAUTHORIZED, "계정에 비정상적인 접근이 감지되었습니다.");



    private final String code;
    private final HttpStatus status;
    private final String message;

    ResponseCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}