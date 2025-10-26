package com.example.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.DeleteMessageUseCase
import com.example.domain.usecase.ObserveMessageDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val observeMessageDetailUseCase: ObserveMessageDetailUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase
) : ContainerHost<DetailState, DetailSideEffect>, ViewModel() {

    override val container = container<DetailState, DetailSideEffect>(DetailState())

    fun onIntent(intent: DetailIntent) = when (intent) {
        is DetailIntent.Load -> loadMessage(intent.id)
        DetailIntent.Refresh -> refreshMessage()
        DetailIntent.Delete -> deleteMessage()
    }

    private var currentMessageId: Int? = null

    private fun loadMessage(id: Int) = intent {
        currentMessageId = id

        reduce { state.copy(isLoading = true, error = null) }

        observeMessageDetailUseCase(id).collect { message ->
            reduce {
                state.copy(
                    message = message,
                    isLoading = false,
                    error = if (message == null) "Message not found" else null
                )
            }
        }
    }

    private fun refreshMessage() = intent {
        currentMessageId?.let { id ->
            reduce { state.copy(isLoading = true) }

            // 여기서 필요하면 서버에서 다시 가져오는 로직 추가
            // refreshMessageUseCase(id) 같은거 만들어서

            reduce { state.copy(isLoading = false) }
            postSideEffect(DetailSideEffect.ShowSnackBar("Refreshed"))
        }
    }

    private fun deleteMessage() = intent {
        state.message?.let { message ->
            reduce { state.copy(isLoading = true) }

            deleteMessageUseCase(message.id)
                .onSuccess {
                    postSideEffect(DetailSideEffect.ShowSnackBar("Message deleted"))
                    postSideEffect(DetailSideEffect.NavigateBack)
                }
                .onFailure { error ->
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(DetailSideEffect.ShowError(error.message ?: "Failed to delete"))
                }
        }
    }
}