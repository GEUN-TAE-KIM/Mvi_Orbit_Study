package com.example.jetpack_mvi_orbit_study.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpack_mvi_orbit_study.ui.detail.DetailScreen
import com.example.jetpack_mvi_orbit_study.ui.home.HomeScreen

/**
 * ■ 모든 목적지 정의
 *
 * - 각 화면은 객체(싱글턴)로 선언
 * - 라우트 파라미터가 있을 경우, companion 의 helper 로 type-safe 빌더 제공
 */
sealed class Screen(val route: String) {

    /** 홈 ─ 파라미터 없음 */
    data object Home : Screen("home")

    /** 상세 ─ id(Int) 하나만 받음 */
    data object Detail : Screen("detail/{id}") {
        // 외부에서 안전하게 라우트 문자열을 만들 수 있도록 헬퍼 함수 제공
        fun route(id: Int) = "detail/$id"
        const val ID_ARG = "id"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        /** Home */
        composable(route = Screen.Home.route) {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate(Screen.Detail.route(id))
                }
            )
        }

        /** Detail(id:Int) */
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(Screen.Detail.ID_ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Screen.Detail.ID_ARG) ?: return@composable
            DetailScreen(id = id)
        }
    }
}