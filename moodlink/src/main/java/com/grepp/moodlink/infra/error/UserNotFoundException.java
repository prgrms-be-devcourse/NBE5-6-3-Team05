package com.grepp.moodlink.infra.error;

import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;

public class UserNotFoundException extends CommonException {

    public UserNotFoundException(ResponseCode code) {
        super(code);
    }

    public UserNotFoundException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
