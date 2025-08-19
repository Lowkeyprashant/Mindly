package com.codewithprashant.mindly.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.codewithprashant.mindly.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    Text(viewModel.welcomeMessage)
}