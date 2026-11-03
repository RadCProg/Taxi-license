package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.dao.QuestionDao
import com.example.data.dao.QuizAttemptDao
import com.example.data.database.DefaultQuestions
import com.example.data.entity.QuestionEntity
import com.example.data.entity.QuizAttemptEntity
import com.example.data.model.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class TaxiQuizRepository(
    private val questionDao: QuestionDao,
    private val quizAttemptDao: QuizAttemptDao,
    context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("taxi_quiz_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(
        AppLanguage.fromCode(prefs.getString(KEY_LANG, AppLanguage.EN.code) ?: AppLanguage.EN.code)
    )
    val currentLanguage = _currentLanguage.asStateFlow()

    private val _showDualSwedish = MutableStateFlow(
        prefs.getBoolean(KEY_DUAL_SWEDISH, true)
    )
    val showDualSwedish = _showDualSwedish.asStateFlow()

    private val _timedModeEnabled = MutableStateFlow(
        prefs.getBoolean(KEY_TIMED_MODE, false)
    )
    val timedModeEnabled = _timedModeEnabled.asStateFlow()

    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        val count = questionDao.getCount()
        if (count == 0) {
            questionDao.insertAll(DefaultQuestions.list)
        }
    }

    fun getAllQuestions(): Flow<List<QuestionEntity>> = questionDao.getAllQuestions()

    fun getQuestionsByCategory(category: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByCategory(category)

    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>> =
        questionDao.getBookmarkedQuestions()

    fun getMistakeQuestions(): Flow<List<QuestionEntity>> =
        questionDao.getMistakeQuestions()

    suspend fun toggleBookmark(id: Int, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        questionDao.setBookmark(id, !currentStatus)
    }

    suspend fun recordAnswer(id: Int, isCorrect: Boolean) = withContext(Dispatchers.IO) {
        questionDao.recordAnswer(id, isCorrect)
    }

    suspend fun saveQuizAttempt(attempt: QuizAttemptEntity): Long = withContext(Dispatchers.IO) {
        quizAttemptDao.insertAttempt(attempt)
    }

    fun getAllAttempts(): Flow<List<QuizAttemptEntity>> = quizAttemptDao.getAllAttempts()

    fun getRecentAttempts(limit: Int = 10): Flow<List<QuizAttemptEntity>> =
        quizAttemptDao.getRecentAttempts(limit)

    suspend fun resetAllStats() = withContext(Dispatchers.IO) {
        questionDao.resetAllStats()
        quizAttemptDao.clearAllAttempts()
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.edit().putString(KEY_LANG, language.code).apply()
    }

    fun setDualSwedish(enabled: Boolean) {
        _showDualSwedish.value = enabled
        prefs.edit().putBoolean(KEY_DUAL_SWEDISH, enabled).apply()
    }

    fun setTimedMode(enabled: Boolean) {
        _timedModeEnabled.value = enabled
        prefs.edit().putBoolean(KEY_TIMED_MODE, enabled).apply()
    }

    companion object {
        private const val KEY_LANG = "key_app_language"
        private const val KEY_DUAL_SWEDISH = "key_dual_swedish"
        private const val KEY_TIMED_MODE = "key_timed_mode"
    }
}
