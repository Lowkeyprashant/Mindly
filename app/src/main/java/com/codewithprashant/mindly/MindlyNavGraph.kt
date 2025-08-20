package com.codewithprashant.mindly.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithprashant.mindly.screens.HomeScreen
import com.codewithprashant.mindly.screens.QuizResultScreen
import com.codewithprashant.mindly.screens.QuizScreen
import com.codewithprashant.mindly.screens.SettingsScreen
import com.codewithprashant.mindly.viewmodel.MainViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Quiz : Screen("quiz")
    object Settings : Screen("settings")
    object QuizResult : Screen("quiz_result")
}

@Composable
fun MindlyNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToQuiz = {
                    navController.navigate(Screen.Quiz.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Quiz.route) {
            QuizScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToResult = {
                    navController.navigate(Screen.QuizResult.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Screen.QuizResult.route) {
            QuizResultScreen(
                onNavigateHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onRetakeQuiz = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                viewModel = viewModel
            )
        }
    }
}