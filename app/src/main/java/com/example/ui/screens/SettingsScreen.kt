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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    showDualSwedish: Boolean,
    timedModeEnabled: Boolean,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleDualSwedish: (Boolean) -> Unit,
    onToggleTimedMode: (Boolean) -> Unit,
    onResetData: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = LocalizedStrings.get("reset_confirm_title", currentLanguage),
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
            },
            text = {
                Text(
                    text = LocalizedStrings.get("reset_confirm_msg", currentLanguage),
                    color = Slate300
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetData()
                        showResetDialog = false
                    }
                ) {
                    Text(
                        text = LocalizedStrings.get("confirm", currentLanguage),
                        color = ErrorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(
                        text = LocalizedStrings.get("cancel", currentLanguage),
                        color = Slate400
                    )
                }
            },
            containerColor = Slate800
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = LocalizedStrings.get("nav_settings", currentLanguage),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Slate100
            )
        }

        // Language Section
        item {
            Text(
                text = LocalizedStrings.get("language_selection", currentLanguage),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TaxiYellow,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LanguageOptionCard(
                    title = "English (Default)",
                    subtitle = "Official study simulator in English",
                    isSelected = currentLanguage == AppLanguage.EN,
                    badge = "EN",
                    onClick = { onSelectLanguage(AppLanguage.EN) }
                )
                LanguageOptionCard(
                    title = "Svenska",
                    subtitle = "Träna på svensk originaltext",
                    isSelected = currentLanguage == AppLanguage.SV,
                    badge = "SV",
                    onClick = { onSelectLanguage(AppLanguage.SV) }
                )
                LanguageOptionCard(
                    title = "العربية",
                    subtitle = "دراسة أسئلة تاكسي السويد باللغة العربية",
                    isSelected = currentLanguage == AppLanguage.AR,
                    badge = "عربي",
                    onClick = { onSelectLanguage(AppLanguage.AR) }
                )
            }
        }

        // Study Preferences Section
        item {
            Text(
                text = if (currentLanguage == AppLanguage.AR) "خيارات الدراسة" else if (currentLanguage == AppLanguage.SV) "Studieinställningar" else "Study Preferences",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TaxiYellow,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Dual Swedish toggle
                PreferenceToggleRow(
                    icon = Icons.Default.MenuBook,
                    title = LocalizedStrings.get("swedish_dual_view", currentLanguage),
                    subtitle = LocalizedStrings.get("dual_view_hint", currentLanguage),
                    checked = showDualSwedish,
                    onCheckedChange = onToggleDualSwedish
                )

                // Timed Mode toggle
                PreferenceToggleRow(
                    icon = Icons.Default.Timer,
                    title = LocalizedStrings.get("timed_mode", currentLanguage),
                    subtitle = LocalizedStrings.get("time_limit", currentLanguage),
                    checked = timedModeEnabled,
                    onCheckedChange = onToggleTimedMode
                )
            }
        }

        // About TFL Exam Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate800),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalTaxi,
                            contentDescription = null,
                            tint = TaxiYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = LocalizedStrings.get("tfl_info_title", currentLanguage),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = LocalizedStrings.get("tfl_info_desc", currentLanguage),
                        fontSize = 13.sp,
                        color = Slate300,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Reset Data Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Slate800)
                    .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .clickable { showResetDialog = true }
                    .padding(16.dp)
                    .testTag("btn_reset_all_data")
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
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = LocalizedStrings.get("reset_data", currentLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                            Text(
                                text = LocalizedStrings.get("reset_confirm_msg", currentLanguage),
                                fontSize = 11.sp,
                                color = Slate400
                            )
                        }
                    }
                }
            }
        }

        // Version Footer
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Taxi Körkort Sverige • Room v1.0",
                    fontSize = 12.sp,
                    color = Slate400
                )
            }
        }
    }
}

@Composable
private fun LanguageOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    badge: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Slate850 else Slate800)
            .border(
                1.5.dp,
                if (isSelected) TaxiYellow else Slate700,
                RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) TaxiYellow else Slate700),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Slate900 else Slate300
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) TaxiYellow else Slate100
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = TaxiYellow,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun PreferenceToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Slate800)
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Slate900),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = TaxiYellow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Slate400,
                        lineHeight = 15.sp
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Slate900,
                    checkedTrackColor = TaxiYellow,
                    uncheckedThumbColor = Slate400,
                    uncheckedTrackColor = Slate900
                )
            )
        }
    }
}
