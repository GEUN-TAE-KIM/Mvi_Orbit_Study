package com.example.domain.error

sealed interface AppError {
    // 네트워크 관련 에러
    sealed interface NetworkError : AppError {
        data class ConnectionFailed(val cause: Throwable? = null) : NetworkError
        data class Timeout(val cause: Throwable? = null) : NetworkError
        data class ServerError(val code: Int) : NetworkError
    }

    // 데이터베이스 관련 에러
    sealed interface DatabaseError : AppError {
        data class ReadFailed(val cause: Throwable? = null) : DatabaseError
        data class WriteFailed(val cause: Throwable? = null) : DatabaseError
        data class NotFound(val id: Int) : DatabaseError
    }

    // 비즈니스 로직 에러
    sealed interface BusinessError : AppError {
        data object EmptyData : BusinessError
        data class InvalidId(val id: Int) : BusinessError
        data class ValidationFailed(val field: String, val reason: ValidationReason) : BusinessError
    }

    // 알 수 없는 에러 (예외 상황)
    data class Unknown(val cause: Throwable) : AppError
}

/**
 * 검증 실패 이유 (타입으로 정의)
 */
enum class ValidationReason {
    REQUIRED,
    TOO_SHORT,
    TOO_LONG,
    INVALID_FORMAT,
    MUST_BE_POSITIVE
}