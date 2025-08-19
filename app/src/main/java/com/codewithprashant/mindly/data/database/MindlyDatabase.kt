package com.codewithprashant.mindly.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codewithprashant.mindly.data.model.Question
import com.codewithprashant.mindly.data.model.QuizResult
import com.codewithprashant.mindly.data.util.Converters

@Database(entities = [Question::class, QuizResult::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MindlyDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}