package com.example.data.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.example.domain.error.AppError
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.ClearAndReloadMessagesUseCase
import javax.inject.Inject

/**
 * 데이터 클리어 후 재로드 UseCase
 *
 * bind()를 사용하여 여러 Either 연산을 체이닝
 * - bind(): Right면 값 추출, Left면 즉시 탈출
 */
class ClearAndReloadMessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : ClearAndReloadMessagesUseCase {

    override suspend fun invoke(): Either<AppError, List<Message>> = either {
        // 1. 데이터 클리어
        repository.clearAllMessages().bind()

        // 2. 새 데이터 로드
        val messages = repository.refreshMessages().bind()

        // 3. 비즈니스 검증
        ensure(messages.isNotEmpty()) {
            AppError.BusinessError.EmptyData
        }

        messages
    }
}