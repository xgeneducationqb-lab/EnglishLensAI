package com.englishlens.ai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = AmberAccent,
    tertiary = EmeraldSuccess,
    background = Slate950,
    surface = Slate900,
    onPrimary = Color.White,
    onSecondary = Slate950,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun EnglishLensAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
