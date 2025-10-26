package com.example.data.datasource

import com.example.data.remote.MessageDto


interface MessageRemoteDataSource {
    suspend fun getMessages(): List<MessageDto>
    suspend fun getMessage(id: Int): MessageDto
    suspend fun deleteMessage(id: Int)
}