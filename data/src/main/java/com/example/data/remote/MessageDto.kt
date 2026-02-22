package com.example.data.remote

import kotlinx.serialization.Serializable

// 네트워크 응답용 데이터 클래스
@Serializable
data class MessageDto(
    val id: Int,
    val title: String,
    val body: String
)

// 💡 DTO를 별도로 만드는 이유
// 1. API 스펙 변경 시 도메인 모델 보호
// 2. 네트워크 응답 형태와 앱 내부 구조 분리
// 3. 직렬화/역직렬화 최적화