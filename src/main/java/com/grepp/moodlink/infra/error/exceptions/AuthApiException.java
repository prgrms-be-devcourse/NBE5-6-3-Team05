package com.grepp.moodlink.infra.error.exceptions;


import com.grepp.moodlink.infra.response.ResponseCode;

public class AuthApiException extends CommonException{

    public AuthApiException(ResponseCode code) {
        super(code);
    }

    public AuthApiException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
