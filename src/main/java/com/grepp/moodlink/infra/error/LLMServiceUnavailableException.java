package com.grepp.moodlink.infra.error;

import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;

public class LLMServiceUnavailableException extends CommonException {

    public LLMServiceUnavailableException(ResponseCode code) {
        super(code);
    }

    public LLMServiceUnavailableException(ResponseCode code, Exception ex) {
        super(code, ex);
    }
}
