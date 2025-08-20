package com.codewithprashant.mindly.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Double,
    val category: String,
    val difficulty: String,
    val completedAt: Long = System.currentTimeMillis(),
    val timeTaken: Long // in milliseconds
) {
    val percentage: Double
        get() = (correctAnswers.toDouble() / totalQuestions) * 100
}