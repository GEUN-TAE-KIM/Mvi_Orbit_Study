package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false  // 개발 단계에서는 false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}

// 💡 Database 설정 설명
// - entities: 데이터베이스에 포함할 Entity들
// - version: 스키마 버전 (변경 시 증가)
// - exportSchema: 스키마 정보 JSON 파일 생성 여부