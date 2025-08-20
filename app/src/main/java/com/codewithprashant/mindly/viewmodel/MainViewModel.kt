package com.codewithprashant.mindly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithprashant.mindly.model.Question
import com.codewithprashant.mindly.model.QuizResult
import com.codewithprashant.mindly.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _currentQuestions = MutableStateFlow<List<Question>>(emptyList())
    val currentQuestions: StateFlow<List<Question>> = _currentQuestions.asStateFlow()

    private val _quizResults = MutableStateFlow<List<QuizResult>>(emptyList())
    val quizResults: StateFlow<List<QuizResult>> = _quizResults.asStateFlow()

    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var quizStartTime = 0L

    val allQuestions = repository.getAllQuestions().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val topScores = repository.getTopScores().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadQuizResults()
        insertSampleQuestionsIfEmpty()
    }

    private fun insertSampleQuestionsIfEmpty() {
        viewModelScope.launch {
            allQuestions.collect { questions ->
                if (questions.isEmpty()) {
                    repository.insertSampleQuestions()
                }
            }
        }
    }

    private fun loadQuizResults() {
        viewModelScope.launch {
            repository.getAllResults().collect {
                _quizResults.value = it
            }
        }
    }

    fun startQuiz(category: String = "All", difficulty: String = "All") {
        viewModelScope.launch {
            val questions = when {
                category != "All" && difficulty != "All" ->
                    repository.getQuestionsByCategoryAndDifficulty(category, difficulty).first()
                category != "All" ->
                    repository.getQuestionsByCategory(category).first()
                difficulty != "All" ->
                    repository.getQuestionsByDifficulty(difficulty).first()
                else -> repository.getAllQuestions().first()
            }.shuffled().take(10) // Take only 10 questions for the quiz

            _currentQuestions.value = questions
            currentQuestionIndex = 0
            correctAnswers = 0
            quizStartTime = System.currentTimeMillis()

            _uiState.update {
                it.copy(
                    isQuizActive = true,
                    currentQuestionIndex = 0,
                    selectedCategory = category,
                    selectedDifficulty = difficulty,
                    showResult = false
                )
            }
        }
    }

    fun submitAnswer(selectedOption: String) {
        val currentQuestion = _currentQuestions.value.getOrNull(currentQuestionIndex)
        currentQuestion?.let { question ->
            if (selectedOption == question.correctAnswer) {
                correctAnswers++
            }

            if (currentQuestionIndex < _currentQuestions.value.size - 1) {
                currentQuestionIndex++
                _uiState.update {
                    it.copy(currentQuestionIndex = currentQuestionIndex)
                }
            } else {
                finishQuiz()
            }
        }
    }

    private fun finishQuiz() {
        val totalQuestions = _currentQuestions.value.size
        val score = (correctAnswers.toDouble() / totalQuestions) * 100
        val timeTaken = System.currentTimeMillis() - quizStartTime

        val result = QuizResult(
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            score = score,
            category = _uiState.value.selectedCategory,
            difficulty = _uiState.value.selectedDifficulty,
            timeTaken = timeTaken
        )

        viewModelScope.launch {
            repository.insertResult(result)
        }

        _uiState.update {
            it.copy(
                isQuizActive = false,
                showResult = true,
                lastQuizResult = result
            )
        }
    }

    fun resetQuiz() {
        _uiState.update {
            QuizUiState()
        }
        _currentQuestions.value = emptyList()
        currentQuestionIndex = 0
        correctAnswers = 0
    }

    fun getCurrentQuestion(): Question? {
        return _currentQuestions.value.getOrNull(currentQuestionIndex)
    }

    fun getProgress(): Float {
        val total = _currentQuestions.value.size
        return if (total > 0) (currentQuestionIndex + 1).toFloat() / total else 0f
    }

    suspend fun getStatistics(): QuizStatistics {
        return QuizStatistics(
            totalQuestions = repository.getTotalQuestions(),
            totalQuizzes = repository.getTotalQuizzesCompleted(),
            averageScore = repository.getAverageScore()
        )
    }
}

data class QuizUiState(
    val isQuizActive: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val selectedCategory: String = "All",
    val selectedDifficulty: String = "All",
    val showResult: Boolean = false,
    val lastQuizResult: QuizResult? = null
)

data class QuizStatistics(
    val totalQuestions: Int,
    val totalQuizzes: Int,
    val averageScore: Double
)