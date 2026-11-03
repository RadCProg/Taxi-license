package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun TopHeader(
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    canGoBack: Boolean = false,
    onBackClick: () -> Unit = {},
    customTitle: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TaxiYellow)
            .statusBarsPadding()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("app_top_header")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (canGoBack) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .testTag("header_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Slate900
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Slate900, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalTaxi,
                            contentDescription = "Taxi Emblem",
                            tint = TaxiYellow,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Text(
                    text = customTitle ?: LocalizedStrings.get("app_title", currentLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Slate900,
                    modifier = Modifier.testTag("header_title_text")
                )
            }

            // Language Toggle Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Slate900)
                    .clickable { onLanguageClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("language_toggle_button"),
                contentAlignment = Alignment.Center
            ) {
                val nextLangText = when (currentLanguage) {
                    AppLanguage.EN -> "SV / عربي"
                    AppLanguage.SV -> "عربي / EN"
                    AppLanguage.AR -> "EN / SV"
                }
                Text(
                    text = nextLangText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TaxiYellow
                )
            }
        }
    }
}
