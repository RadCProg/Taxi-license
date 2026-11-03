package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate850
import com.example.ui.theme.TaxiYellow
import com.example.ui.viewmodel.ScreenState

@Composable
fun BottomNavBar(
    currentScreen: ScreenState,
    currentLanguage: AppLanguage,
    onTabSelected: (ScreenState) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Slate850)
            .navigationBarsPadding()
            .testTag("app_bottom_nav_bar")
    ) {
        // Subtle top border divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Slate700)
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = LocalizedStrings.get("nav_home", currentLanguage),
                isSelected = currentScreen == ScreenState.HOME,
                testTag = "nav_tab_home",
                onClick = { onTabSelected(ScreenState.HOME) }
            )

            NavItem(
                icon = Icons.Default.Leaderboard,
                label = LocalizedStrings.get("nav_stats", currentLanguage),
                isSelected = currentScreen == ScreenState.STATS,
                testTag = "nav_tab_stats",
                onClick = { onTabSelected(ScreenState.STATS) }
            )

            NavItem(
                icon = Icons.Default.Bookmark,
                label = LocalizedStrings.get("nav_review", currentLanguage),
                isSelected = currentScreen == ScreenState.REVIEW,
                testTag = "nav_tab_review",
                onClick = { onTabSelected(ScreenState.REVIEW) }
            )

            NavItem(
                icon = Icons.Default.Settings,
                label = LocalizedStrings.get("nav_settings", currentLanguage),
                isSelected = currentScreen == ScreenState.SETTINGS,
                testTag = "nav_tab_settings",
                onClick = { onTabSelected(ScreenState.SETTINGS) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val color = if (isSelected) TaxiYellow else Slate400

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = TaxiYellow),
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = color,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
