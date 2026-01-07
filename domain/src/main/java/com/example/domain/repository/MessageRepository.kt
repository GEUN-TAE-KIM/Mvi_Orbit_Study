package com.example.domain.repository

import com.example.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages(): Flow<List<Message>>
    fun observeMessage(id: Int): Flow<Message?>
    suspend fun refreshMessages(): Result<Unit>
    suspend fun deleteMessage(id: Int): Result<Unit>
    suspend fun clearAllMessages(): Result<Unit>
}
