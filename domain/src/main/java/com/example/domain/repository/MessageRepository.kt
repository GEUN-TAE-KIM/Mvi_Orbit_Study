package com.example.domain.repository

import arrow.core.Either
import com.example.domain.error.AppError
import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages(): Flow<List<Message>>
    fun observeMessage(id: Int): Flow<Message?>

    suspend fun refreshMessages(): Either<AppError, List<Message>>
    suspend fun deleteMessage(id: Int): Either<AppError, Unit>
    suspend fun clearAllMessages(): Either<AppError, Unit>
}
