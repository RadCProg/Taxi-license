package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.QuestionEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TaxiYellow

@Composable
fun ReviewScreen(
    currentLanguage: AppLanguage,
    showDualSwedish: Boolean,
    mistakes: List<QuestionEntity>,
    bookmarks: List<QuestionEntity>,
    onStartMistakesQuiz: () -> Unit,
    onStartBookmarksQuiz: () -> Unit,
    onToggleBookmark: (Int, Boolean) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val activeList = if (selectedTab == 0) mistakes else bookmarks
    val tab1Title = "${LocalizedStrings.get("mistake_bank", currentLanguage)} (${mistakes.size})"
    val tab2Title = "${LocalizedStrings.get("bookmarked_questions", currentLanguage)} (${bookmarks.size})"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("review_screen")
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = LocalizedStrings.get("nav_review", currentLanguage),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Slate850,
            contentColor = TaxiYellow,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = TaxiYellow
                )
            },
            divider = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate700)
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = tab1Title,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) TaxiYellow else Slate400
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = tab2Title,
                        fontSize = 13.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) TaxiYellow else Slate400
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action button to practice current tab
        if (activeList.isNotEmpty()) {
            Button(
                onClick = if (selectedTab == 0) onStartMistakesQuiz else onStartBookmarksQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("btn_practice_review"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TaxiYellow,
                    contentColor = Slate900
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (selectedTab == 0) {
                        LocalizedStrings.get("btn_review_mistakes", currentLanguage)
                    } else {
                        if (currentLanguage == AppLanguage.AR) "تدريب على الأسئلة المحفوظة" else if (currentLanguage == AppLanguage.SV) "Öva på sparade frågor" else "Practice Saved Questions"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // List of questions
        if (activeList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Slate800)
                    .border(1.dp, Slate700, RoundedCornerShape(16.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Default.CheckCircle else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = if (selectedTab == 0) SuccessGreen else TaxiYellow,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = if (selectedTab == 0) {
                            LocalizedStrings.get("no_mistakes", currentLanguage)
                        } else {
                            LocalizedStrings.get("no_bookmarks", currentLanguage)
                        },
                        fontSize = 14.sp,
                        color = Slate300,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activeList) { question ->
                    ReviewQuestionCard(
                        question = question,
                        currentLanguage = currentLanguage,
                        showDualSwedish = showDualSwedish,
                        onToggleBookmark = { onToggleBookmark(question.id, question.isBookmarked) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ReviewQuestionCard(
    question: QuestionEntity,
    currentLanguage: AppLanguage,
    showDualSwedish: Boolean,
    onToggleBookmark: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Slate800)
            .border(1.dp, Slate700, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate900)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = question.category.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TaxiYellow
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (question.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (question.isBookmarked) TaxiYellow else Slate400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = question.getQuestion(currentLanguage),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100,
                lineHeight = 20.sp
            )

            if (showDualSwedish && currentLanguage != AppLanguage.SV) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "🇸🇪 ${question.questionSv}",
                    fontSize = 12.sp,
                    color = Slate400,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Correct Answer banner
            val correctText = question.getOptions(currentLanguage)[question.correctOptionIndex]
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SuccessGreen.copy(alpha = 0.15f))
                    .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = correctText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation
            Text(
                text = question.getExplanation(currentLanguage),
                fontSize = 12.sp,
                color = Slate300,
                lineHeight = 16.sp
            )
        }
    }
}
