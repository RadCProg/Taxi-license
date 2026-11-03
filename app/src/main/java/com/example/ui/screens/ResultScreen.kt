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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TaxiYellow
import com.example.ui.viewmodel.ActiveQuizState

@Composable
fun ResultScreen(
    quizState: ActiveQuizState,
    currentLanguage: AppLanguage,
    onTryAgain: () -> Unit,
    onGoHome: () -> Unit
) {
    val total = quizState.questions.size
    val score = quizState.score
    val percentage = if (total > 0) (score * 100) / total else 0
    val isPass = percentage >= 75

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState)
            .testTag("result_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Large Score Circle
        val ringColor = if (isPass) SuccessGreen else TaxiYellow
        Box(
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
                .background(Slate800)
                .border(6.dp, ringColor, CircleShape)
                .testTag("result_score_circle"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$score/$total",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Slate100
                )
                Text(
                    text = "$percentage%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ringColor
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title
        Text(
            text = LocalizedStrings.get("results_title", currentLanguage),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Status Message with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = if (isPass) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isPass) SuccessGreen else TaxiYellow,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = if (isPass) {
                    LocalizedStrings.get("results_pass", currentLanguage)
                } else {
                    LocalizedStrings.get("results_fail", currentLanguage)
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Slate300
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Card Breakdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Slate800)
                .border(1.dp, Slate700, RoundedCornerShape(16.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Correct Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = LocalizedStrings.get("correct", currentLanguage),
                        color = Slate400,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "$score",
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate700)
                )

                // Incorrect Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = LocalizedStrings.get("wrong", currentLanguage),
                        color = Slate400,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${total - score}",
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed,
                        fontSize = 16.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Slate700)
                )

                // Accuracy Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = LocalizedStrings.get("accuracy", currentLanguage),
                        color = Slate400,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "$percentage%",
                        fontWeight = FontWeight.Bold,
                        color = Slate100,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onTryAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_try_again"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TaxiYellow,
                    contentColor = Slate900
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = LocalizedStrings.get("btn_try_again", currentLanguage),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onGoHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_back_home"),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Slate100
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = LocalizedStrings.get("btn_back_home", currentLanguage),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
