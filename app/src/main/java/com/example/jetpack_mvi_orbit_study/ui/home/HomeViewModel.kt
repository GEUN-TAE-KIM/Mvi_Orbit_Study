package com.example.jetpack_mvi_orbit_study.ui.home

import androidx.lifecycle.ViewModel
import com.example.jetpack_mvi_orbit_study.data.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MessageRepository
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {

    override val container = container<HomeState, HomeSideEffect>(HomeState())

    init {
        observeDb()
    }

    private fun observeDb() = intent {
        repo.messages.collect { list ->
            reduce { state.copy(items = list) }
        }
    }

    fun onIntent(i: HomeIntent) = when (i) {
        HomeIntent.Load -> refresh()
        HomeIntent.Refresh -> refresh()
    }

    private fun refresh() = intent {
        reduce { state.copy(isRefreshing = true) }
        val result = repo.refresh()
        result.onFailure { postSideEffect(HomeSideEffect.Error(it.localizedMessage ?: "Unknown")) }
        reduce { state.copy(isRefreshing = false) }
    }
}