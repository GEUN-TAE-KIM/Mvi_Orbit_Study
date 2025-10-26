package com.example.data.usecase

import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import com.example.domain.usecase.ObserveMessagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveMessagesUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : ObserveMessagesUseCase {

    override fun invoke(): Flow<List<Message>> {
        return repository.observeMessages()
            .distinctUntilChanged()
    }
}