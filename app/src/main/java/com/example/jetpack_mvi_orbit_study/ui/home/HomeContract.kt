package com.example.jetpack_mvi_orbit_study.ui.home

import com.example.jetpack_mvi_orbit_study.model.Message

// 지금 화면이 어떤 모양이어야 하는지 를 100 % 기술
data class HomeState(
    val items: List<Message> = emptyList(),
    val isRefreshing: Boolean = false
)

// 무엇을 하라 는 명령/이벤트
sealed interface HomeIntent {
    data object Load : HomeIntent
    data object Refresh : HomeIntent
}

// 한 번만 실행되는 외부 행동
sealed interface HomeSideEffect {
    data class Error(val message: String) : HomeSideEffect
}