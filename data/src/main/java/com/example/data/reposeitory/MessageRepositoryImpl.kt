package com.example.data.reposeitory

import arrow.core.Either
import arrow.core.raise.either
import com.example.data.datasource.MessageLocalDataSource
import com.example.data.datasource.MessageRemoteDataSource
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.error.AppError
import com.example.domain.model.Message
import com.example.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
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

    /**
     * 서버에서 메시지를 가져와 로컬 DB에 저장
     *
     * either { } 블록 안에서:
     * - 성공하면 자동으로 Either.Right로 감싸짐
     * - 예외 발생시 catch로 잡아서 Either.Left로 변환
     */
    override suspend fun refreshMessages(): Either<AppError, List<Message>> = either {
        // try-catch를 사용해 예외를 AppError로 변환
        // TODO resultOf 로 바꾸기
        val remoteMessages = try {
            remoteDataSource.getMessages()
        } catch (e: SocketTimeoutException) {
            raise(AppError.NetworkError.Timeout(e))
        } catch (e: IOException) {
            raise(AppError.NetworkError.ConnectionFailed(e))
        } catch (e: HttpException) {
            raise(AppError.NetworkError.ServerError(e.code()))
        } catch (e: Exception) {
            raise(AppError.Unknown(e))
        }

        // 로컬 DB에 저장
        try {
            val entities = remoteMessages.map { it.toEntity() }
            localDataSource.insertAll(entities)
        } catch (e: Exception) {
            raise(AppError.DatabaseError.WriteFailed(e))
        }

        // 3. 도메인 모델로 변환하여 반환
        remoteMessages.map { it.toDomain() }
    }

    /**
     * 메시지 삭제 (Optimistic Update)
     */
    override suspend fun deleteMessage(id: Int): Either<AppError, Unit> = either {
        // 1. 로컬 먼저 삭제 (Optimistic Update)
        try {
            localDataSource.deleteById(id)
        } catch (e: Exception) {
            raise(AppError.DatabaseError.WriteFailed(e))
        }

        // 2. 서버 삭제
        try {
            remoteDataSource.deleteMessage(id)
        } catch (e: SocketTimeoutException) {
            raise(AppError.NetworkError.Timeout(e))
        } catch (e: IOException) {
            raise(AppError.NetworkError.ConnectionFailed(e))
        } catch (e: HttpException) {
            raise(AppError.NetworkError.ServerError(e.code()))
        } catch (e: Exception) {
            raise(AppError.Unknown(e))
        }
    }

    override suspend fun clearAllMessages(): Either<AppError, Unit> = either {
        try {
            localDataSource.deleteAll()
        } catch (e: Exception) {
            raise(AppError.DatabaseError.WriteFailed(e))
        }
    }
}
