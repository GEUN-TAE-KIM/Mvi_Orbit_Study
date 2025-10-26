package com.example.data.usecase

import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.RefreshMessagesUseCase
import javax.inject.Inject

class RefreshMessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : RefreshMessagesUseCase {

    override suspend fun invoke(): Result<Unit> {
        return repository.refreshMessages()
    }
}