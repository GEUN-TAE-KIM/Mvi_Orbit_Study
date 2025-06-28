package com.example.jetpack_mvi_orbit_study.data

import com.example.jetpack_mvi_orbit_study.data.local.MessageDao
import com.example.jetpack_mvi_orbit_study.data.remote.MessageApi
import com.example.jetpack_mvi_orbit_study.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val api: MessageApi,
    private val dao: MessageDao
) {
    val messages: Flow<List<Message>> =
        dao.getAllFlow()
            .map { list -> list.map { it.toDomain() } }
            .distinctUntilChanged()

    fun message(id: Int): Flow<Message?> =
        dao.findFlow(id).map { it?.toDomain() }

    /** 네트워크 → DB 동기화 */
    suspend fun refresh(): Result<Unit> = runCatching {
        val remote = api.getMessages()
        dao.insertAll(remote.map { it.toEntity() })
    }
}