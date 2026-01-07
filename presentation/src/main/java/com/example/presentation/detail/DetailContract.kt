package com.example.presentation.detail

import com.example.domain.model.Message

data class DetailState(
    val message: Message? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface DetailIntent {
    data class Load(val id: Int) : DetailIntent
    data object Refresh : DetailIntent
    data object Delete : DetailIntent
}

sealed interface DetailSideEffect {
    data class ShowSnackBar(val message: String) : DetailSideEffect
    data class ShowError(val message: String) : DetailSideEffect
    data object NavigateBack : DetailSideEffect
}