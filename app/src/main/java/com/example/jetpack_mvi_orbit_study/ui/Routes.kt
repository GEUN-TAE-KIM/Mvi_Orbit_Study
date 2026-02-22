package com.example.jetpack_mvi_orbit_study.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.presentation.detail.DetailScreen
import com.example.presentation.home.HomeScreen
import kotlinx.serialization.Serializable

/**
 * ■ 모든 목적지 정의 (Type-Safe Navigation)
 *
 * - @Serializable 로 선언하면 Navigation이 직렬화/역직렬화를 자동 처리
 * - 문자열 라우트, navArgument, NavType 설정 불필요
 */

/** 홈 ─ 파라미터 없음 */
@Serializable
data object Home

/** 상세 ─ id(Int) 하나만 받음 */
@Serializable
data class Detail(val id: Int)

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {

        /** Home */
        composable<Home> {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate(Detail(id))
                }
            )
        }

        /** Detail */
        composable<Detail> { backStackEntry ->
            val detail: Detail = backStackEntry.toRoute()
            DetailScreen(
                id = detail.id,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
