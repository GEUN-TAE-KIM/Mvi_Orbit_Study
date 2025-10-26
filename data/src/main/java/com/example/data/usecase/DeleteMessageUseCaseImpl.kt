package com.example.data.usecase

import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.DeleteMessageUseCase
import javax.inject.Inject

class DeleteMessageUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : DeleteMessageUseCase {

    override suspend fun invoke(id: Int): Result<Unit> {
        return repository.deleteMessage(id)
    }
}