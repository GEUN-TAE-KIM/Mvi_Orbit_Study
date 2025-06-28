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