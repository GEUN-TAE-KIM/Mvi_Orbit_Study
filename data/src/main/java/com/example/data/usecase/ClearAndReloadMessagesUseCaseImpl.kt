package com.example.data.usecase

import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.ClearAndReloadMessagesUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class ClearAndReloadMessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : ClearAndReloadMessagesUseCase {

    override suspend fun invoke(): Result<Unit> {
        return runCatching {
            // 데이터 클리어
            repository.clearAllMessages().getOrThrow()

            // UX를 위한 딜레이
            delay(1000)

            // 새 데이터 로드
            repository.refreshMessages().getOrThrow()
        }
    }
}