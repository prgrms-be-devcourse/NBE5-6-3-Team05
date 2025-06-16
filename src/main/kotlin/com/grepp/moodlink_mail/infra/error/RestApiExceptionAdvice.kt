package com.grepp.moodlink_mail.infra.error

import com.grepp.moodlink_mail.infra.error.exceptions.CommonException
import com.grepp.moodlink_mail.infra.response.ApiResponse
import com.grepp.moodlink_mail.infra.response.ResponseCode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.ui.Model
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestApiExceptionAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validatorHandler(ex: MethodArgumentNotValidException)
    : ResponseEntity<ApiResponse<Map<String, String?>>> {
        log.info(ex.message, ex)
        val errors: MutableMap<String, String?> = LinkedHashMap()
        ex.fieldErrors.forEach { errors[it.field] = it.defaultMessage }
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.error(ResponseCode.BAD_REQUEST, errors))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotSupportedHandler(ex: HttpRequestMethodNotSupportedException):
            ResponseEntity<ApiResponse<String?>> {
        return ResponseEntity
            .badRequest()
            .body(ApiResponse.error(ResponseCode.BAD_REQUEST, ex.message))
    }

    @ExceptionHandler(CommonException::class)
    fun restApiExceptionHandler(ex: CommonException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(ex.code().status())
            .body(ApiResponse.error(ex.code()))
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun authorizationDeniedHandler(
        ex: AuthorizationDeniedException,
        model: Model
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(ResponseCode.UNAUTHORIZED))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsExHandler(
        ex: BadCredentialsException,
        model: Model
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(ResponseCode.BAD_CREDENTIAL))
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeExceptionHandler(ex: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .internalServerError()
            .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR))
    }
}
