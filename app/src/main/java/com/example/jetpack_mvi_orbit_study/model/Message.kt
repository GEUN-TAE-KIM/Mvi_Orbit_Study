package com.example.jetpack_mvi_orbit_study.model

// 도메인 모델 - 앱 전체에서 사용하는 순수한 데이터 구조
data class Message(
    val id: Int,
    val title: String,
    val body: String
)

// 💡 도메인 모델의 특징
// - 외부 의존성 없음 (Android/Room/Retrofit 등)
// - 비즈니스 로직의 핵심 데이터 구조
// - 불변성 유지 (val만 사용)