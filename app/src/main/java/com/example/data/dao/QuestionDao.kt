package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE category = :category ORDER BY id ASC")
    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1 ORDER BY id ASC")
    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE lastAnsweredCorrectly = 0 ORDER BY id ASC")
    fun getMistakeQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("UPDATE questions SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmark(id: Int, bookmarked: Boolean)

    @Query("UPDATE questions SET timesAnswered = timesAnswered + 1, timesCorrect = timesCorrect + (CASE WHEN :isCorrect THEN 1 ELSE 0 END), lastAnsweredCorrectly = :isCorrect WHERE id = :id")
    suspend fun recordAnswer(id: Int, isCorrect: Boolean)

    @Query("UPDATE questions SET timesAnswered = 0, timesCorrect = 0, lastAnsweredCorrectly = NULL")
    suspend fun resetAllStats()
}
