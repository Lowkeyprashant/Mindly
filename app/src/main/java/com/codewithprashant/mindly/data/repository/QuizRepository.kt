package com.codewithprashant.mindly.data.repository

import com.codewithprashant.mindly.data.database.QuizDao
import com.codewithprashant.mindly.data.model.QuizResult
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    suspend fun saveQuizResult(result: QuizResult) {
        quizDao.insertQuizResult(result)
    }

    fun getRecentResults() = quizDao.getRecentQuizResults()
}