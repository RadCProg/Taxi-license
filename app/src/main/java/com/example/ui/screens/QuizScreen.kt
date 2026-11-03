package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TaxiYellow
import com.example.ui.viewmodel.ActiveQuizState

@Composable
fun QuizScreen(
    quizState: ActiveQuizState,
    currentLanguage: AppLanguage,
    showDualSwedish: Boolean,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onToggleBookmark: (Int, Boolean) -> Unit
) {
    val currentQuestion = quizState.currentQuestion ?: return
    val totalQuestions = quizState.questions.size
    val currentIndex = quizState.currentIndex
    val options = currentQuestion.getOptions(currentLanguage)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(scrollState)
            .testTag("quiz_screen")
    ) {
        // Progress Row & Timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val progressText = String.format(
                LocalizedStrings.get("question_of", currentLanguage),
                currentIndex + 1,
                totalQuestions
            )
            Text(
                text = progressText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                modifier = Modifier.testTag("quiz_progress_text")
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (quizState.remainingSeconds > 0) {
                    val minutes = quizState.remainingSeconds / 60
                    val seconds = quizState.remainingSeconds % 60
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Slate800)
                            .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = TaxiYellow,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TaxiYellow
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Bookmark Icon
                IconButton(
                    onClick = { onToggleBookmark(currentQuestion.id, currentQuestion.isBookmarked) },
                    modifier = Modifier.testTag("btn_toggle_bookmark")
                ) {
                    Icon(
                        imageVector = if (currentQuestion.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark Question",
                        tint = if (currentQuestion.isBookmarked) TaxiYellow else Slate400
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Linear Progress Bar
        LinearProgressIndicator(
            progress = { quizState.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = TaxiYellow,
            trackColor = Slate800,
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Question Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate850)
                .border(1.dp, Slate700, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = currentQuestion.getQuestion(currentLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100,
                    lineHeight = 24.sp,
                    modifier = Modifier.testTag("question_text")
                )

                // Optional Dual Swedish display if non-Swedish language is selected
                if (showDualSwedish && currentLanguage != AppLanguage.SV) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Slate900.copy(alpha = 0.8f))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "🇸🇪 Svenska:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TaxiYellow
                            )
                            Text(
                                text = currentQuestion.questionSv,
                                fontSize = 13.sp,
                                color = Slate300,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Options List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEachIndexed { index, optionText ->
                val isSelected = quizState.selectedOptionIndex == index
                val isCorrectOption = currentQuestion.correctOptionIndex == index

                val (bgColor, borderColor, textColor) = when {
                    !quizState.hasAnswered -> Triple(Slate800, Slate700, Slate100)
                    isCorrectOption -> Triple(SuccessGreen, SuccessGreen, Color.White)
                    isSelected && !isCorrectOption -> Triple(ErrorRed, ErrorRed, Color.White)
                    else -> Triple(Slate850.copy(alpha = 0.6f), Slate700.copy(alpha = 0.5f), Slate400)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable(enabled = !quizState.hasAnswered) {
                            onOptionSelected(index)
                        }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("option_item_$index")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = optionText,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            modifier = Modifier.weight(1f),
                            lineHeight = 20.sp
                        )

                        if (quizState.hasAnswered) {
                            if (isCorrectOption) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Incorrect",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Explanation Box (visible after answering)
        AnimatedVisibility(
            visible = quizState.hasAnswered,
            enter = fadeIn() + slideInVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate800)
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                        .testTag("explanation_box")
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = TaxiYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = LocalizedStrings.get("explanation_title", currentLanguage),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TaxiYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentQuestion.getExplanation(currentLanguage),
                            fontSize = 13.sp,
                            color = Slate300,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }

        // Next / Finish Button
        AnimatedVisibility(
            visible = quizState.hasAnswered,
            enter = fadeIn()
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_next_question"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TaxiYellow,
                        contentColor = Slate900
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (quizState.isLastQuestion) {
                            LocalizedStrings.get("finish_test", currentLanguage)
                        } else {
                            LocalizedStrings.get("next_question", currentLanguage)
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
