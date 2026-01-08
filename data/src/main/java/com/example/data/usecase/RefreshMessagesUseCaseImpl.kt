package com.example.data.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.example.domain.error.AppError
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.RefreshMessagesUseCase
import javax.inject.Inject

class RefreshMessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : RefreshMessagesUseCase {

    override suspend fun invoke(): Either<AppError, List<Message>> = either {
        // 1. Repository 호출
        val messages = repository.refreshMessages().bind()

        // 2. 비즈니스 검증: 데이터가 비어있으면 에러
        ensure(messages.isNotEmpty()) {
            AppError.BusinessError.EmptyData
        }

        // 3. 검증 통과 시 반환
        messages
    }
}