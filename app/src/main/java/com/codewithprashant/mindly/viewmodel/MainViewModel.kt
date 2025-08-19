package com.codewithprashant.mindly.viewmodel

import androidx.lifecycle.ViewModel
import com.codewithprashant.mindly.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {
    val welcomeMessage = "This is a ViewModel-powered message!"
}