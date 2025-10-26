package com.example.data.usecase

import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.ObserveMessageDetailUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessageDetailUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : ObserveMessageDetailUseCase {

    override fun invoke(id: Int): Flow<Message?> {
        return repository.observeMessage(id)
    }
}