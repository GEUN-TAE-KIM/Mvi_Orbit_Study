package com.example.jetpack_mvi_orbit_study.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpack_mvi_orbit_study.R
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    showTopBar: Boolean = false,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collectLatest { sideEffect ->
            if (sideEffect is HomeSideEffect.Error)
                snackBarHostState.showSnackbar(sideEffect.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            if (showTopBar) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.title_messages)) },
                    actions = {
                        IconButton(onClick = { viewModel.onIntent(HomeIntent.Refresh) }) {
                            Icon(Icons.Outlined.Refresh, contentDescription = null)
                        }
                    }
                )
            }
        }
    ) { padding ->

        when {
            state.isRefreshing -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            state.items.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text(stringResource(R.string.msg_no_data)) }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 8.dp, end = 8.dp,
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
                ) {
                    items(
                        items = state.items,
                        key = { it.id }
                    ) { msg ->
                        MessageRow(
                            title = msg.title,
                            body = msg.body,
                            onClick = { onItemClick(msg.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageRow(
    title: String,
    body: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(body, maxLines = 1) },
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
    HorizontalDivider()
}
