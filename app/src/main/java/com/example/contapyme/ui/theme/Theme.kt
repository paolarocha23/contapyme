package com.example.contapyme.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = BlueSoft,
    onSecondary = Navy,
    background = Color(0xFF0B1229),
    onBackground = Color.White,
    surface = Color(0xFF111A3D),
    onSurface = Color.White,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = Navy,
    onSecondary = Color.White,
    tertiary = SuccessGreen,
    background = BackgroundApp,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    error = ErrorRed,
    onError = Color.White,
    outline = BorderSoft
)

@Composable
fun ContaPymeTheme(
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
