package com.example.jetpack_mvi_orbit_study.ui.detail

import androidx.lifecycle.ViewModel
import com.example.jetpack_mvi_orbit_study.data.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: MessageRepository
) : ContainerHost<DetailState, DetailSideEffect>, ViewModel() {

    override val container = container<DetailState, DetailSideEffect>(DetailState())

    fun onIntent(i: DetailIntent) =
        when (i) {
            is DetailIntent.Load -> intent {
                repo.message(i.id).collect { msg ->
                    reduce { state.copy(message = msg) }
                }
            }
        }
}