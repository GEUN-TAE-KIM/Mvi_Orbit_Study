package com.example.jetpack_mvi_orbit_study.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY id DESC")
    fun getAllFlow(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE id = :id")
    fun findFlow(id: Int): Flow<MessageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MessageEntity>)
}

// 💡 Room DAO 특징
// - Flow 반환: 데이터 변경 시 자동 업데이트
// - suspend 함수: 백그라운드 스레드에서 실행
// - OnConflictStrategy.REPLACE: 중복 시 덮어쓰기