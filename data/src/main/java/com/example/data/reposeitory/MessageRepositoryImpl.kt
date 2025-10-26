package com.example.data.reposeitory

import com.example.data.datasource.MessageLocalDataSource
import com.example.data.datasource.MessageRemoteDataSource
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val localDataSource: MessageLocalDataSource,
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {

    override fun observeMessages(): Flow<List<Message>> {
        return localDataSource.observeAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override fun observeMessage(id: Int): Flow<Message?> {
        return localDataSource.observeById(id)
            .map { entity ->
                entity?.toDomain()
            }
    }

    override suspend fun refreshMessages(): Result<Unit> {
        return runCatching {
            val remoteMessages = remoteDataSource.getMessages()
            val entities = remoteMessages.map { it.toEntity() }
            localDataSource.insertAll(entities)
        }
    }

    override suspend fun deleteMessage(id: Int): Result<Unit> {
        return runCatching {
            // Optimistic update - 로컬 먼저 삭제
            localDataSource.deleteById(id)

            // 서버 삭제
            remoteDataSource.deleteMessage(id)
        }
    }

    override suspend fun clearAllMessages(): Result<Unit> {
        return runCatching {
            localDataSource.deleteAll()
        }
    }
}
