package com.codewithprashant.mindly.di

import android.content.Context
import androidx.room.Room
import com.codewithprashant.mindly.data.database.MindlyDatabase
import com.codewithprashant.mindly.data.database.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindlyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MindlyDatabase::class.java,
            "mindly_database"
        ).build()
    }

    @Provides
    fun provideQuizDao(database: MindlyDatabase): QuizDao {
        return database.quizDao()
    }
}