package com.codewithprashant.mindly.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.codewithprashant.mindly.model.Question
import com.codewithprashant.mindly.model.QuizResult
import com.codewithprashant.mindly.util.Converters

@Database(
    entities = [Question::class, QuizResult::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MindlyDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: MindlyDatabase? = null

        fun getDatabase(context: Context): MindlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MindlyDatabase::class.java,
                    "mindly_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate database with sample questions if needed
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}