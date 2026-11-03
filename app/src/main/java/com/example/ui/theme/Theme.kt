package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TaxiYellow,
    onPrimary = Slate900,
    primaryContainer = TaxiYellowDark,
    onPrimaryContainer = Slate900,
    secondary = AccentBlue,
    onSecondary = Slate900,
    background = Slate900,
    onBackground = Slate100,
    surface = Slate800,
    onSurface = Slate100,
    surfaceVariant = Slate850,
    onSurfaceVariant = Slate300,
    outline = Slate700,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = TaxiYellow,
    onPrimary = Slate900,
    primaryContainer = TaxiYellowLight,
    onPrimaryContainer = Slate900,
    secondary = AccentBlue,
    onSecondary = Color.White,
    background = Slate900, // Keep signature dark slate aesthetic for the taxi simulator
    onBackground = Slate100,
    surface = Slate800,
    onSurface = Slate100,
    surfaceVariant = Slate850,
    onSurfaceVariant = Slate300,
    outline = Slate700,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun TaxiKorkortTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
