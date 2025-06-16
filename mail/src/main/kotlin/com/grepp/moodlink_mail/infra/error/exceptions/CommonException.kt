package com.grepp.moodlink_mail.infra.error.exceptions

import com.grepp.moodlink_mail.infra.response.ResponseCode
import org.slf4j.LoggerFactory

open class CommonException : RuntimeException {
    private val code: ResponseCode
    private var redirect = "/"
    private val log = LoggerFactory.getLogger(javaClass)

    constructor(code: ResponseCode) {
        this.code = code
    }

    constructor(code: ResponseCode, redirect: String) {
        this.code = code
        this.redirect = redirect
    }

    constructor(code: ResponseCode, e: Exception) {
        this.code = code
        log.error(e.message, e)
    }

    constructor(code: ResponseCode, e: Exception, redirect: String) {
        this.code = code
        this.redirect = redirect
        log.error(e.message, e)
    }

    fun redirect(): String {
        return redirect
    }

    fun code(): ResponseCode {
        return code
    }
}
