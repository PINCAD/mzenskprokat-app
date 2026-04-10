package com.mzenskprokat.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Accent = Color(0xFF19B5C3)

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9F5F8),
    onPrimaryContainer = Color(0xFF08363A),

    secondary = Color(0xFF4B5B60),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE7EFF1),
    onSecondaryContainer = Color(0xFF1E2A2E),

    tertiary = Color(0xFF0F1720),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE8ECEF),
    onTertiaryContainer = Color(0xFF111827),

    background = Color(0xFFF6F8F9),
    onBackground = Color(0xFF111315),

    surface = Color.White,
    onSurface = Color(0xFF111315),
    surfaceVariant = Color(0xFFF0F4F5),
    onSurfaceVariant = Color(0xFF5C676B),

    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFFD5DDE0),
    outlineVariant = Color(0xFFE6ECEE)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF48D1DB),
    onPrimary = Color(0xFF00363B),
    primaryContainer = Color(0xFF004F56),
    onPrimaryContainer = Color(0xFF97F0F6),

    secondary = Color(0xFFB7C8CD),
    onSecondary = Color(0xFF223236),
    secondaryContainer = Color(0xFF394A4F),
    onSecondaryContainer = Color(0xFFD3E4E9),

    tertiary = Color(0xFFD4DCE0),
    onTertiary = Color(0xFF263238),
    tertiaryContainer = Color(0xFF3B474D),
    onTertiaryContainer = Color(0xFFEAF1F4),

    background = Color(0xFF0F1416),
    onBackground = Color(0xFFE7ECEE),

    surface = Color(0xFF151B1D),
    onSurface = Color(0xFFE7ECEE),
    surfaceVariant = Color(0xFF222A2D),
    onSurfaceVariant = Color(0xFFBCC7CB),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF3E484C),
    outlineVariant = Color(0xFF2A3235)
)

@Composable
fun MzenskProkatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}