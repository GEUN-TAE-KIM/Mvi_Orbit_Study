package com.example.jetpack_mvi_orbit_study.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String
)

// 💡 Room 어노테이션 설명
// - @Entity: 테이블 정의
// - @PrimaryKey: 기본 키 설정
// - tableName: 실제 테이블 이름 지정