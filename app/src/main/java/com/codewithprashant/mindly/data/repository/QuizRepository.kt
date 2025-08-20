package com.codewithprashant.mindly.repository

import com.codewithprashant.mindly.data.database.QuizDao
import com.codewithprashant.mindly.model.Question
import com.codewithprashant.mindly.model.QuizResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {

    // Question operations
    fun getAllQuestions(): Flow<List<Question>> = quizDao.getAllQuestions()

    fun getQuestionsByCategory(category: String): Flow<List<Question>> =
        quizDao.getQuestionsByCategory(category)

    fun getQuestionsByDifficulty(difficulty: String): Flow<List<Question>> =
        quizDao.getQuestionsByDifficulty(difficulty)

    fun getQuestionsByCategoryAndDifficulty(category: String, difficulty: String): Flow<List<Question>> =
        quizDao.getQuestionsByCategoryAndDifficulty(category, difficulty)

    suspend fun getQuestionById(id: Int): Question? = quizDao.getQuestionById(id)

    suspend fun insertQuestion(question: Question) = quizDao.insertQuestion(question)

    suspend fun insertQuestions(questions: List<Question>) = quizDao.insertQuestions(questions)

    suspend fun updateQuestion(question: Question) = quizDao.updateQuestion(question)

    suspend fun deleteQuestion(question: Question) = quizDao.deleteQuestion(question)

    suspend fun deleteAllQuestions() = quizDao.deleteAllQuestions()

    // Quiz Result operations
    fun getAllResults(): Flow<List<QuizResult>> = quizDao.getAllResults()

    fun getResultsByCategory(category: String): Flow<List<QuizResult>> =
        quizDao.getResultsByCategory(category)

    fun getTopScores(): Flow<List<QuizResult>> = quizDao.getTopScores()

    suspend fun insertResult(result: QuizResult) = quizDao.insertResult(result)

    suspend fun deleteResult(result: QuizResult) = quizDao.deleteResult(result)

    suspend fun deleteAllResults() = quizDao.deleteAllResults()

    // Statistics
    suspend fun getAverageScore(): Double = quizDao.getAverageScore()

    suspend fun getTotalQuizzesCompleted(): Int = quizDao.getTotalQuizzesCompleted()

    suspend fun getTotalQuestions(): Int = quizDao.getTotalQuestions()

    // Sample data for testing/demo
    suspend fun insertSampleQuestions() {
        val sampleQuestions = listOf(
            Question(
                question = "What is the capital of France?",
                optionA = "London",
                optionB = "Berlin",
                optionC = "Paris",
                optionD = "Madrid",
                correctAnswer = "C",
                category = "Geography",
                difficulty = "Easy"
            ),
            Question(
                question = "Who painted the Mona Lisa?",
                optionA = "Van Gogh",
                optionB = "Leonardo da Vinci",
                optionC = "Picasso",
                optionD = "Michelangelo",
                correctAnswer = "B",
                category = "Art",
                difficulty = "Medium"
            ),
            Question(
                question = "What is the largest planet in our solar system?",
                optionA = "Earth",
                optionB = "Mars",
                optionC = "Jupiter",
                optionD = "Saturn",
                correctAnswer = "C",
                category = "Science",
                difficulty = "Easy"
            ),
            Question(
                question = "In what year did World War II end?",
                optionA = "1944",
                optionB = "1945",
                optionC = "1946",
                optionD = "1947",
                correctAnswer = "B",
                category = "History",
                difficulty = "Medium"
            ),
            Question(
                question = "What is the chemical symbol for gold?",
                optionA = "Go",
                optionB = "Gd",
                optionC = "Au",
                optionD = "Ag",
                correctAnswer = "C",
                category = "Science",
                difficulty = "Hard"
            )
        )
        insertQuestions(sampleQuestions)
    }
}