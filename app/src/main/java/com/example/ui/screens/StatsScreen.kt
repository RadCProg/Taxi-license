package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.QuestionEntity
import com.example.data.entity.QuizAttemptEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TaxiYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatsScreen(
    currentLanguage: AppLanguage,
    attempts: List<QuizAttemptEntity>,
    allQuestions: List<QuestionEntity>,
    onStartTest: () -> Unit
) {
    val totalAttempts = attempts.size
    val passedAttempts = attempts.count { it.isPassed }
    val passRate = if (totalAttempts > 0) (passedAttempts * 100) / totalAttempts else 0
    val totalMastered = allQuestions.count { it.timesCorrect > 0 && it.lastAnsweredCorrectly == true }

    val readinessText = if (passRate >= 75 && totalAttempts >= 3) {
        LocalizedStrings.get("ready_badge", currentLanguage)
    } else {
        LocalizedStrings.get("needs_study", currentLanguage)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("stats_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = LocalizedStrings.get("nav_stats", currentLanguage),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate100
            )
        }

        // Readiness Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stats_readiness_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Slate800),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = LocalizedStrings.get("readiness_score", currentLanguage),
                                fontSize = 14.sp,
                                color = Slate400,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = readinessText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (passRate >= 75 && totalAttempts >= 3) SuccessGreen else TaxiYellow
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(TaxiYellow.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = TaxiYellow,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape)
                            .background(Slate900)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = (passRate.coerceIn(0, 100) / 100f))
                                .height(10.dp)
                                .clip(CircleShape)
                                .background(if (passRate >= 75) SuccessGreen else TaxiYellow)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "0%",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                        Text(
                            text = "Trafikverket Target: 75%",
                            fontSize = 11.sp,
                            color = TaxiYellow
                        )
                        Text(
                            text = "100%",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }
            }
        }

        // Summary Metric Tiles
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricTile(
                    modifier = Modifier.weight(1f),
                    label = LocalizedStrings.get("total_tests", currentLanguage),
                    value = "$totalAttempts",
                    color = AccentBlue
                )
                MetricTile(
                    modifier = Modifier.weight(1f),
                    label = LocalizedStrings.get("pass_rate", currentLanguage),
                    value = "$passRate%",
                    color = if (passRate >= 75) SuccessGreen else TaxiYellow
                )
                MetricTile(
                    modifier = Modifier.weight(1f),
                    label = "Mastered",
                    value = "$totalMastered/${allQuestions.size}",
                    color = SuccessGreen
                )
            }
        }

        // Recent Tests Header
        item {
            Text(
                text = if (currentLanguage == AppLanguage.AR) "سجل الاختبارات الأخيرة" else if (currentLanguage == AppLanguage.SV) "Senaste Provresultat" else "Recent Test History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (attempts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Slate800)
                        .border(1.dp, Slate700, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.AR) "لم تقم بأي اختبار بعد" else if (currentLanguage == AppLanguage.SV) "Inga prov gjorda än" else "No tests taken yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.AR) "ابدأ تدريبك الآن لمشاهدة تقدمك" else if (currentLanguage == AppLanguage.SV) "Börja öva för att se din utveckling" else "Start practicing to track your score progression",
                            fontSize = 12.sp,
                            color = Slate400
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onStartTest,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TaxiYellow,
                                contentColor = Slate900
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(LocalizedStrings.get("start_test", currentLanguage), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(attempts) { attempt ->
                AttemptItemRow(attempt = attempt, currentLanguage = currentLanguage)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MetricTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Slate800)
            .border(1.dp, Slate700, RoundedCornerShape(16.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Slate400,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AttemptItemRow(
    attempt: QuizAttemptEntity,
    currentLanguage: AppLanguage
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(attempt.timestamp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Slate850)
            .border(1.dp, Slate700, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (attempt.isPassed) SuccessGreen.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (attempt.isPassed) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (attempt.isPassed) SuccessGreen else ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    val catName = if (attempt.category == "mock_exam") {
                        LocalizedStrings.get("start_mock_exam", currentLanguage)
                    } else {
                        attempt.category.replaceFirstChar { it.uppercase() }
                    }
                    Text(
                        text = catName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = dateString,
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${attempt.score}/${attempt.totalQuestions}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (attempt.isPassed) SuccessGreen else TaxiYellow
                )
                Text(
                    text = "${attempt.percentage}%",
                    fontSize = 12.sp,
                    color = Slate300
                )
            }
        }
    }
}
