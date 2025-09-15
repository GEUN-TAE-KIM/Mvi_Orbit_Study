package com.example.jetpack_mvi_orbit_study.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailScreen(
    id: Int,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()

    // 화면 진입 시 해당 ID의 메시지 로드
    LaunchedEffect(id) { viewModel.onIntent(DetailIntent.Load(id)) }

    // 💡 LaunchedEffect(id) 사용 이유
    // - id가 변경될 때마다 실행
    // - 다른 메시지 상세로 이동 시 새로운 데이터 로드

    state.message?.let { msg ->
        Column(Modifier.padding(24.dp)) {
            Text(msg.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(msg.body)
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}