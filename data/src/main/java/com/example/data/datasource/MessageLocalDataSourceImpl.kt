package com.example.data.datasource

import com.example.data.local.MessageDao
import com.example.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageLocalDataSource {

    override fun observeAll(): Flow<List<MessageEntity>> {
        return messageDao.getAllFlow()
    }

    override fun observeById(id: Int): Flow<MessageEntity?> {
        return messageDao.findFlow(id)
    }

    override suspend fun insertAll(messages: List<MessageEntity>) {
        messageDao.insertAll(messages)
    }

    override suspend fun deleteById(id: Int) {
        messageDao.deleteById(id)
    }

    override suspend fun deleteAll() {
        messageDao.deleteAll()
    }
}