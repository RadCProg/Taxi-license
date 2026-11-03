package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopHeader
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ReviewScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiKorkortTheme
import com.example.ui.viewmodel.ScreenState
import com.example.ui.viewmodel.TaxiQuizViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TaxiQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaxiKorkortTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: TaxiQuizViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val showDualSwedish by viewModel.showDualSwedish.collectAsStateWithLifecycle()
    val timedModeEnabled by viewModel.timedModeEnabled.collectAsStateWithLifecycle()
    val allQuestions by viewModel.allQuestions.collectAsStateWithLifecycle()
    val bookmarkedQuestions by viewModel.bookmarkedQuestions.collectAsStateWithLifecycle()
    val mistakeQuestions by viewModel.mistakeQuestions.collectAsStateWithLifecycle()
    val allAttempts by viewModel.allAttempts.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()

    val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    // Back handling
    BackHandler(enabled = currentScreen != ScreenState.HOME) {
        viewModel.navigateTo(ScreenState.HOME)
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Slate900),
            topBar = {
                val isSubScreen = currentScreen == ScreenState.QUIZ || currentScreen == ScreenState.RESULTS
                val customTitle = when (currentScreen) {
                    ScreenState.QUIZ -> quizState.title
                    ScreenState.RESULTS -> null
                    else -> null
                }
                TopHeader(
                    currentLanguage = currentLanguage,
                    onLanguageClick = { viewModel.toggleLanguage() },
                    canGoBack = isSubScreen,
                    onBackClick = { viewModel.navigateTo(ScreenState.HOME) },
                    customTitle = customTitle
                )
            },
            bottomBar = {
                // Show bottom bar when on main tabs
                if (currentScreen != ScreenState.QUIZ && currentScreen != ScreenState.RESULTS) {
                    BottomNavBar(
                        currentScreen = currentScreen,
                        currentLanguage = currentLanguage,
                        onTabSelected = { viewModel.navigateTo(it) }
                    )
                }
            },
            containerColor = Slate900
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Slate900)
            ) {
                when (currentScreen) {
                    ScreenState.HOME -> {
                        HomeScreen(
                            currentLanguage = currentLanguage,
                            allQuestions = allQuestions,
                            onSelectCategory = { category ->
                                viewModel.startQuizForCategory(category)
                            },
                            onStartMockExam = {
                                viewModel.startMockExam()
                            },
                            onStartQuickQuiz = {
                                viewModel.startQuickQuiz()
                            }
                        )
                    }

                    ScreenState.QUIZ -> {
                        QuizScreen(
                            quizState = quizState,
                            currentLanguage = currentLanguage,
                            showDualSwedish = showDualSwedish,
                            onOptionSelected = { index ->
                                viewModel.selectOption(index)
                            },
                            onNextQuestion = {
                                viewModel.nextQuestion()
                            },
                            onToggleBookmark = { id, currentStatus ->
                                viewModel.toggleBookmark(id, currentStatus)
                            }
                        )
                    }

                    ScreenState.RESULTS -> {
                        ResultScreen(
                            quizState = quizState,
                            currentLanguage = currentLanguage,
                            onTryAgain = {
                                if (quizState.isMockExam) {
                                    viewModel.startMockExam()
                                } else {
                                    viewModel.startQuickQuiz()
                                }
                            },
                            onGoHome = {
                                viewModel.navigateTo(ScreenState.HOME)
                            }
                        )
                    }

                    ScreenState.STATS -> {
                        StatsScreen(
                            currentLanguage = currentLanguage,
                            attempts = allAttempts,
                            allQuestions = allQuestions,
                            onStartTest = {
                                viewModel.navigateTo(ScreenState.HOME)
                            }
                        )
                    }

                    ScreenState.REVIEW -> {
                        ReviewScreen(
                            currentLanguage = currentLanguage,
                            showDualSwedish = showDualSwedish,
                            mistakes = mistakeQuestions,
                            bookmarks = bookmarkedQuestions,
                            onStartMistakesQuiz = {
                                viewModel.startMistakeBankQuiz()
                            },
                            onStartBookmarksQuiz = {
                                viewModel.startBookmarksQuiz()
                            },
                            onToggleBookmark = { id, currentStatus ->
                                viewModel.toggleBookmark(id, currentStatus)
                            }
                        )
                    }

                    ScreenState.SETTINGS -> {
                        SettingsScreen(
                            currentLanguage = currentLanguage,
                            showDualSwedish = showDualSwedish,
                            timedModeEnabled = timedModeEnabled,
                            onSelectLanguage = { lang ->
                                viewModel.setLanguage(lang)
                            },
                            onToggleDualSwedish = { enabled ->
                                viewModel.setDualSwedish(enabled)
                            },
                            onToggleTimedMode = { enabled ->
                                viewModel.setTimedMode(enabled)
                            },
                            onResetData = {
                                viewModel.resetAllData()
                            }
                        )
                    }
                }
            }
        }
    }
}
