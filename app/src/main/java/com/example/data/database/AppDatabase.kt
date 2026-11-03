package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.QuestionDao
import com.example.data.dao.QuizAttemptDao
import com.example.data.entity.QuestionEntity
import com.example.data.entity.QuizAttemptEntity

@Database(
    entities = [QuestionEntity::class, QuizAttemptEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun quizAttemptDao(): QuizAttemptDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taxi_korkort_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
