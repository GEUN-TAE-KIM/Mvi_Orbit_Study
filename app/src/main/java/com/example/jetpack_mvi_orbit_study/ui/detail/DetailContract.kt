package com.example.jetpack_mvi_orbit_study.ui.detail

import com.example.jetpack_mvi_orbit_study.model.Message

data class DetailState(
    val message: Message? = null,
)

sealed interface DetailIntent {
    data class Load(val id: Int) : DetailIntent
}

sealed interface DetailSideEffect