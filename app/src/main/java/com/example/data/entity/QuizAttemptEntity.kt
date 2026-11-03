package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val isPassed: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val timeTakenSeconds: Int = 0,
    val languageUsed: String = "en"
)
