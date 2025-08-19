package com.codewithprashant.mindly.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.codewithprashant.mindly.data.model.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC LIMIT 5")
    fun getRecentQuizResults(): Flow<List<QuizResult>>
}