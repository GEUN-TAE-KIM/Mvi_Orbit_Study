package com.example.data.datasource

import com.example.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageLocalDataSource {
    fun observeAll(): Flow<List<MessageEntity>>
    fun observeById(id: Int): Flow<MessageEntity?>
    suspend fun insertAll(messages: List<MessageEntity>)
    suspend fun deleteById(id: Int)
    suspend fun deleteAll()
}