package com.example.jetpack_mvi_orbit_study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.jetpack_mvi_orbit_study.ui.AppNavGraph
import com.example.jetpack_mvi_orbit_study.ui.theme.Jetpack_mvi_orbit_studyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Jetpack_mvi_orbit_studyTheme {
                AppNavGraph(navController = navController)
            }
        }
    }
}

// 💡 @AndroidEntryPoint 역할
// - Hilt가 이 Activity에 의존성을 주입할 수 있게 함
// - ViewModel, Repository 등을 자동으로 생성
// - 생명주기에 맞춰 의존성 관리

// 💡 rememberNavController() 역할
// - NavController 인스턴스 생성 및 기억
// - 컴포지션 중에 상태 유지
// - 화면 전환 시 백스택 관리