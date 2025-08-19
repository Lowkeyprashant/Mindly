package com.codewithprashant.mindly

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.codewithprashant.mindly.screens.HomeScreen
import com.codewithprashant.mindly.screens.QuizScreen
import com.codewithprashant.mindly.screens.SettingsScreen
import com.codewithprashant.mindly.viewmodel.MainViewModel

object Destinations {
    const val HOME_ROUTE = "home"
    const val SETTINGS_ROUTE = "settings"
    const val QUIZ_ROUTE = "quiz"
}

@Composable
fun MindlyNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME_ROUTE
    ) {
        composable(Destinations.HOME_ROUTE) {
            val viewModel: MainViewModel = hiltViewModel()
            HomeScreen(viewModel = viewModel)
        }
        composable(Destinations.SETTINGS_ROUTE) {
            SettingsScreen()
        }
        composable(Destinations.QUIZ_ROUTE) {
            QuizScreen()
        }
    }
}