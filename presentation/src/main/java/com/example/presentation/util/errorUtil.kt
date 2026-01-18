package com.example.presentation.util

import com.example.domain.error.AppError
import com.example.domain.error.ValidationReason

// 메세지 확장함수 메세지는 string.xml에 해두는게 좋음
fun AppError.toUserMessage(): String = when (this) {
    // 네트워크 에러
    is AppError.NetworkError.ConnectionFailed ->
        "네트워크 연결에 실패했습니다. 인터넷 연결을 확인해주세요."
    is AppError.NetworkError.Timeout ->
        "요청 시간이 초과되었습니다. 다시 시도해주세요."
    is AppError.NetworkError.ServerError -> when (code) {
        in 500..599 -> "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
        401 -> "인증이 필요합니다. 다시 로그인해주세요."
        403 -> "접근 권한이 없습니다."
        404 -> "요청한 정보를 찾을 수 없습니다."
        else -> "서버 오류가 발생했습니다. (코드: $code)"
    }

    // 데이터베이스 에러
    is AppError.DatabaseError.ReadFailed ->
        "데이터를 불러오는 중 오류가 발생했습니다."
    is AppError.DatabaseError.WriteFailed ->
        "데이터를 저장하는 중 오류가 발생했습니다."
    is AppError.DatabaseError.NotFound ->
        "요청한 데이터를 찾을 수 없습니다."

    // 비즈니스 에러
    AppError.BusinessError.EmptyData ->
        "표시할 데이터가 없습니다."
    is AppError.BusinessError.InvalidId ->
        "잘못된 ID입니다."
    is AppError.BusinessError.ValidationFailed ->
        "${field.toFieldName()}: ${reason.toMessage()}"

    // 알 수 없는 에러
    is AppError.Unknown ->
        "알 수 없는 오류가 발생했습니다."
}

private fun String.toFieldName(): String = when (this) {
    "id" -> "ID"
    "title" -> "제목"
    "body" -> "내용"
    "email" -> "이메일"
    "password" -> "비밀번호"
    else -> this
}

private fun ValidationReason.toMessage(): String = when (this) {
    ValidationReason.REQUIRED -> "필수 입력 항목입니다"
    ValidationReason.TOO_SHORT -> "너무 짧습니다"
    ValidationReason.TOO_LONG -> "너무 깁니다"
    ValidationReason.INVALID_FORMAT -> "형식이 올바르지 않습니다"
    ValidationReason.MUST_BE_POSITIVE -> "0보다 커야 합니다"
}