package com.codewithprashant.mindly.data.database

import androidx.room.*
import com.codewithprashant.mindly.model.Question
import com.codewithprashant.mindly.model.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    // Question operations
    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE difficulty = :difficulty")
    fun getQuestionsByDifficulty(difficulty: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE category = :category AND difficulty = :difficulty")
    fun getQuestionsByCategoryAndDifficulty(category: String, difficulty: String): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): Question?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM questions")
    suspend fun deleteAllQuestions()

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getTotalQuestions(): Int

    // Quiz Result operations
    @Query("SELECT * FROM quiz_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results WHERE category = :category ORDER BY completedAt DESC")
    fun getResultsByCategory(category: String): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<QuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResult)

    @Delete
    suspend fun deleteResult(result: QuizResult)

    @Query("DELETE FROM quiz_results")
    suspend fun deleteAllResults()

    // Statistics
    @Query("SELECT AVG(score) FROM quiz_results")
    suspend fun getAverageScore(): Double

    @Query("SELECT COUNT(*) FROM quiz_results")
    suspend fun getTotalQuizzesCompleted(): Int
}