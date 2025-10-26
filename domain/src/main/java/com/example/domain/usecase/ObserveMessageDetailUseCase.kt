package com.example.domain.usecase

import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ObserveMessageDetailUseCase {
    operator fun invoke(id: Int): Flow<Message?>
}