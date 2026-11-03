package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.entity.QuestionEntity
import com.example.data.entity.QuizAttemptEntity
import com.example.data.model.AppLanguage
import com.example.data.model.QuestionCategory
import com.example.data.repository.TaxiQuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenState {
    HOME,
    QUIZ,
    RESULTS,
    STATS,
    REVIEW,
    SETTINGS
}

data class UserAnswerRecord(
    val question: QuestionEntity,
    val selectedIndex: Int,
    val isCorrect: Boolean
)

data class ActiveQuizState(
    val title: String = "",
    val categoryId: String = "",
    val questions: List<QuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val hasAnswered: Boolean = false,
    val score: Int = 0,
    val answersHistory: List<UserAnswerRecord> = emptyList(),
    val startTime: Long = 0L,
    val remainingSeconds: Int = 25 * 60,
    val isMockExam: Boolean = false
) {
    val currentQuestion: QuestionEntity?
        get() = questions.getOrNull(currentIndex)

    val progress: Float
        get() = if (questions.isNotEmpty()) (currentIndex + 1).toFloat() / questions.size.toFloat() else 0f

    val isLastQuestion: Boolean
        get() = questions.isNotEmpty() && currentIndex == questions.size - 1
}

class TaxiQuizViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaxiQuizRepository

    val currentLanguage: StateFlow<AppLanguage>
    val showDualSwedish: StateFlow<Boolean>
    val timedModeEnabled: StateFlow<Boolean>

    val allQuestions: StateFlow<List<QuestionEntity>>
    val bookmarkedQuestions: StateFlow<List<QuestionEntity>>
    val mistakeQuestions: StateFlow<List<QuestionEntity>>
    val allAttempts: StateFlow<List<QuizAttemptEntity>>

    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _quizState = MutableStateFlow(ActiveQuizState())
    val quizState: StateFlow<ActiveQuizState> = _quizState.asStateFlow()

    private var timerJob: Job? = null

    init {
        val database = AppDatabase.getInstance(application)
        repository = TaxiQuizRepository(
            questionDao = database.questionDao(),
            quizAttemptDao = database.quizAttemptDao(),
            context = application
        )

        currentLanguage = repository.currentLanguage
        showDualSwedish = repository.showDualSwedish
        timedModeEnabled = repository.timedModeEnabled

        allQuestions = repository.getAllQuestions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        bookmarkedQuestions = repository.getBookmarkedQuestions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        mistakeQuestions = repository.getMistakeQuestions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allAttempts = repository.getAllAttempts().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun setLanguage(language: AppLanguage) {
        repository.setLanguage(language)
    }

    fun toggleLanguage() {
        val next = when (currentLanguage.value) {
            AppLanguage.EN -> AppLanguage.SV
            AppLanguage.SV -> AppLanguage.AR
            AppLanguage.AR -> AppLanguage.EN
        }
        setLanguage(next)
    }

    fun setDualSwedish(enabled: Boolean) {
        repository.setDualSwedish(enabled)
    }

    fun setTimedMode(enabled: Boolean) {
        repository.setTimedMode(enabled)
    }

    fun startQuizForCategory(category: QuestionCategory) {
        val list = allQuestions.value.filter { it.category == category.id }
        val questionsToUse = if (list.isEmpty()) allQuestions.value else list
        initQuiz(
            title = category.title(currentLanguage.value),
            categoryId = category.id,
            questions = questionsToUse.shuffled(),
            isMock = false
        )
    }

    fun startMockExam() {
        // TFL full simulation across all questions
        val pool = allQuestions.value.shuffled()
        initQuiz(
            title = QuestionCategory.MOCK_EXAM.title(currentLanguage.value),
            categoryId = "mock_exam",
            questions = pool,
            isMock = true
        )
    }

    fun startQuickQuiz() {
        val pool = allQuestions.value.shuffled().take(10)
        initQuiz(
            title = "Quick 10 Quiz",
            categoryId = "quick_quiz",
            questions = pool,
            isMock = false
        )
    }

    fun startMistakeBankQuiz() {
        val list = mistakeQuestions.value.shuffled()
        if (list.isNotEmpty()) {
            initQuiz(
                title = "Mistake Bank Review",
                categoryId = "mistakes",
                questions = list,
                isMock = false
            )
        }
    }

    fun startBookmarksQuiz() {
        val list = bookmarkedQuestions.value.shuffled()
        if (list.isNotEmpty()) {
            initQuiz(
                title = "Saved Bookmarks",
                categoryId = "bookmarks",
                questions = list,
                isMock = false
            )
        }
    }

    private fun initQuiz(
        title: String,
        categoryId: String,
        questions: List<QuestionEntity>,
        isMock: Boolean
    ) {
        timerJob?.cancel()
        _quizState.value = ActiveQuizState(
            title = title,
            categoryId = categoryId,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            hasAnswered = false,
            score = 0,
            answersHistory = emptyList(),
            startTime = System.currentTimeMillis(),
            remainingSeconds = if (isMock || timedModeEnabled.value) 25 * 60 else 0,
            isMockExam = isMock
        )
        _currentScreen.value = ScreenState.QUIZ

        if (isMock || timedModeEnabled.value) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_quizState.value.remainingSeconds > 0) {
                delay(1000)
                val currentSec = _quizState.value.remainingSeconds
                if (currentSec <= 1) {
                    _quizState.value = _quizState.value.copy(remainingSeconds = 0)
                    finishQuiz()
                    break
                } else {
                    _quizState.value = _quizState.value.copy(remainingSeconds = currentSec - 1)
                }
            }
        }
    }

    fun selectOption(index: Int) {
        val state = _quizState.value
        if (state.hasAnswered) return
        val currentQ = state.currentQuestion ?: return

        val isCorrect = index == currentQ.correctOptionIndex
        val newScore = if (isCorrect) state.score + 1 else state.score

        val newRecord = UserAnswerRecord(
            question = currentQ,
            selectedIndex = index,
            isCorrect = isCorrect
        )

        _quizState.value = state.copy(
            selectedOptionIndex = index,
            hasAnswered = true,
            score = newScore,
            answersHistory = state.answersHistory + newRecord
        )

        viewModelScope.launch {
            repository.recordAnswer(currentQ.id, isCorrect)
        }
    }

    fun toggleBookmark(questionId: Int, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(questionId, currentStatus)
            // If current active question matches, update state
            val currentQ = _quizState.value.currentQuestion
            if (currentQ != null && currentQ.id == questionId) {
                val updatedQuestions = _quizState.value.questions.map {
                    if (it.id == questionId) it.copy(isBookmarked = !currentStatus) else it
                }
                _quizState.value = _quizState.value.copy(questions = updatedQuestions)
            }
        }
    }

    fun nextQuestion() {
        val state = _quizState.value
        if (state.isLastQuestion) {
            finishQuiz()
        } else {
            _quizState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedOptionIndex = null,
                hasAnswered = false
            )
        }
    }

    fun finishQuiz() {
        timerJob?.cancel()
        val state = _quizState.value
        val total = state.questions.size
        val score = state.score
        val percentage = if (total > 0) (score * 100) / total else 0
        val isPassed = percentage >= 75 // Swedish TFL test passing standard (approx 75%)
        val durationSeconds = ((System.currentTimeMillis() - state.startTime) / 1000).toInt()

        viewModelScope.launch {
            repository.saveQuizAttempt(
                QuizAttemptEntity(
                    category = state.categoryId,
                    score = score,
                    totalQuestions = total,
                    percentage = percentage,
                    isPassed = isPassed,
                    timeTakenSeconds = durationSeconds,
                    languageUsed = currentLanguage.value.code
                )
            )
        }

        _currentScreen.value = ScreenState.RESULTS
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllStats()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
