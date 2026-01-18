package com.example.data.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.example.domain.error.AppError
import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.DeleteMessageUseCase
import javax.inject.Inject

class DeleteMessageUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : DeleteMessageUseCase {

    override suspend fun invoke(id: Int): Either<AppError, Unit> = either {
        // 1. 비즈니스 검증: ID 유효성
        ensure(id > 0) {
            AppError.BusinessError.InvalidId(id)
        }

        // 2. Repository 호출
        repository.deleteMessage(id).bind()
    }
}