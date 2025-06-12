package com.grepp.moodlink.infra.error.exceptions;

import com.grepp.moodlink.infra.response.ResponseCode;

public class AuthWebException extends CommonException {

    public AuthWebException(ResponseCode code) {
        super(code);
    }

    public AuthWebException(ResponseCode code, String redirect) {
        super(code, redirect);
    }

    public AuthWebException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
