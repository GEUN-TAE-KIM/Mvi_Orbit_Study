package com.example.data.datasource

import com.example.data.remote.MessageApi
import com.example.data.remote.MessageDto
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(
    private val api: MessageApi
) : MessageRemoteDataSource {

    override suspend fun getMessages(): List<MessageDto> {
        return api.getMessages()
    }

    override suspend fun getMessage(id: Int): MessageDto {
        return api.getMessage(id)
    }

    override suspend fun deleteMessage(id: Int) {
        api.deleteMessage(id)
    }
}