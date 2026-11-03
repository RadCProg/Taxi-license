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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.QuestionEntity
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.data.model.QuestionCategory
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TaxiYellow
import com.example.ui.theme.TaxiYellowDark

@Composable
fun HomeScreen(
    currentLanguage: AppLanguage,
    allQuestions: List<QuestionEntity>,
    onSelectCategory: (QuestionCategory) -> Unit,
    onStartMockExam: () -> Unit,
    onStartQuickQuiz: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Welcome Header
            Column {
                Text(
                    text = LocalizedStrings.get("home_welcome", currentLanguage),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Slate100,
                    modifier = Modifier.testTag("home_welcome_text")
                )
                Text(
                    text = LocalizedStrings.get("home_subtitle", currentLanguage),
                    fontSize = 14.sp,
                    color = Slate400,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Hero Card: Mock Exam
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_mock_exam_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Slate800),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Slate850, Slate800)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(TaxiYellow.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assignment,
                                    contentDescription = null,
                                    tint = TaxiYellow,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = LocalizedStrings.get("start_mock_exam", currentLanguage),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Text(
                                    text = LocalizedStrings.get("time_limit", currentLanguage),
                                    fontSize = 12.sp,
                                    color = TaxiYellow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = LocalizedStrings.get("tfl_info_desc", currentLanguage),
                            fontSize = 12.sp,
                            color = Slate300,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onStartMockExam,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_start_full_exam"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TaxiYellow,
                                    contentColor = Slate900
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LocalizedStrings.get("start_test", currentLanguage),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Button(
                                onClick = onStartQuickQuiz,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_start_quick_quiz"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Slate700,
                                    contentColor = Slate100
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LocalizedStrings.get("quick_random_quiz", currentLanguage),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Subject Categories
        item {
            Text(
                text = if (currentLanguage == AppLanguage.AR) "الأقسام والمواضيع" else if (currentLanguage == AppLanguage.SV) "Ämnesområden" else "Study Topics & Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Category 1: Law
        item {
            val count = allQuestions.count { it.category == QuestionCategory.LAW.id }
            CategoryItem(
                category = QuestionCategory.LAW,
                icon = Icons.Default.Gavel,
                iconBg = AccentBlue.copy(alpha = 0.2f),
                iconColor = AccentBlue,
                count = count,
                currentLanguage = currentLanguage,
                onClick = { onSelectCategory(QuestionCategory.LAW) }
            )
        }

        // Category 2: Safety
        item {
            val count = allQuestions.count { it.category == QuestionCategory.SAFETY.id }
            CategoryItem(
                category = QuestionCategory.SAFETY,
                icon = Icons.Default.Security,
                iconBg = SuccessGreen.copy(alpha = 0.2f),
                iconColor = SuccessGreen,
                count = count,
                currentLanguage = currentLanguage,
                onClick = { onSelectCategory(QuestionCategory.SAFETY) }
            )
        }

        // Category 3: Navigation
        item {
            val count = allQuestions.count { it.category == QuestionCategory.NAVIGATION.id }
            CategoryItem(
                category = QuestionCategory.NAVIGATION,
                icon = Icons.Default.Map,
                iconBg = TaxiYellow.copy(alpha = 0.2f),
                iconColor = TaxiYellow,
                count = count,
                currentLanguage = currentLanguage,
                onClick = { onSelectCategory(QuestionCategory.NAVIGATION) }
            )
        }

        // Category 4: Equipment & Taximeter
        item {
            val count = allQuestions.count { it.category == QuestionCategory.EQUIPMENT.id }
            CategoryItem(
                category = QuestionCategory.EQUIPMENT,
                icon = Icons.Default.ElectricMeter,
                iconBg = AccentPurple.copy(alpha = 0.2f),
                iconColor = AccentPurple,
                count = count,
                currentLanguage = currentLanguage,
                onClick = { onSelectCategory(QuestionCategory.EQUIPMENT) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CategoryItem(
    category: QuestionCategory,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    count: Int,
    currentLanguage: AppLanguage,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Slate800)
            .border(1.dp, Slate700, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .testTag("category_item_${category.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title(currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
                Text(
                    text = category.description(currentLanguage),
                    fontSize = 12.sp,
                    color = Slate400,
                    modifier = Modifier.padding(top = 2.dp),
                    lineHeight = 16.sp
                )
                Text(
                    text = String.format(LocalizedStrings.get("questions_count", currentLanguage), count),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TaxiYellowDark,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Slate400,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
