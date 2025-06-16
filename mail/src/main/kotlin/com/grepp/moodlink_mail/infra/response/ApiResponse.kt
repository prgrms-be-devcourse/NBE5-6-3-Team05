package com.grepp.moodlink_mail.infra.response

@JvmRecord
data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ResponseCode.OK.code(), ResponseCode.OK.message(), data)
        }

        fun noContent(): ApiResponse<Unit> {
            return ApiResponse(ResponseCode.OK.code(), ResponseCode.OK.message())
        }

        @JvmStatic
        fun error(code: ResponseCode): ApiResponse<Unit> {
            return ApiResponse(code.code(), code.message())
        }

        fun <T> error(code: ResponseCode, data: T): ApiResponse<T> {
            return ApiResponse(code.code(), code.message(), data)
        }
    }
}
