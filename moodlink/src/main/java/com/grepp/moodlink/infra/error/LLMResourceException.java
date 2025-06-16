package com.grepp.moodlink.infra.error;

import com.grepp.moodlink.infra.error.exceptions.CommonException;
import com.grepp.moodlink.infra.response.ResponseCode;

public class LLMResourceException extends CommonException {
    public LLMResourceException(ResponseCode code) {
        super(code);
    }

    public LLMResourceException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
