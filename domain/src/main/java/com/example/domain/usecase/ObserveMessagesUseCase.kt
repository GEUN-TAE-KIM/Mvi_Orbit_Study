package com.example.domain.usecase

import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ObserveMessagesUseCase {
    operator fun invoke(): Flow<List<Message>>
}