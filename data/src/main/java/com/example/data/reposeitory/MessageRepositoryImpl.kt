package com.example.data.reposeitory

import android.database.sqlite.SQLiteException
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

        // 1. 원격 데이터 가져오기
        val remoteMessages = Either.catch { remoteDataSource.getMessages() }
            .mapLeft { e -> e.toNetworkError() }
            .bind()

        // 로컬 DB에 저장
        Either.catch { localDataSource.insertAll(remoteMessages.map { it.toEntity() }) }
            .mapLeft { it.toDatabaseError() }
            .bind()

        // 3. 도메인 모델로 변환하여 반환
        remoteMessages.map { it.toDomain() }
    }

    /**
     * 메시지 삭제 (Optimistic Update)
     */
    override suspend fun deleteMessage(id: Int): Either<AppError, Unit> = either {

        // 1. 로컬 삭제
        Either.catch { localDataSource.deleteById(id) }
            .mapLeft { it.toDatabaseError() }
            .bind()

        // 2. 서버 삭제
        Either.catch { remoteDataSource.deleteMessage(id) }
            .mapLeft { it.toNetworkError() }
            .bind()
    }

    override suspend fun clearAllMessages(): Either<AppError, Unit> = either {
        Either.catch { localDataSource.deleteAll() }
            .mapLeft { it.toDatabaseError() }
            .bind()
    }

    // 예외 변환 확장함수
    private fun Throwable.toNetworkError(): AppError = when (this) {
        is SocketTimeoutException -> AppError.NetworkError.Timeout(this)
        is IOException -> AppError.NetworkError.ConnectionFailed(this)
        is HttpException -> AppError.NetworkError.ServerError(code())
        else -> AppError.Unknown(this)
    }

    private fun Throwable.toDatabaseError(): AppError = when (this) {
        is SQLiteException -> AppError.DatabaseError.WriteFailed(this)
        else -> AppError.DatabaseError.WriteFailed(this)
    }
}
