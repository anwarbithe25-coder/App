package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val FossDarkColorScheme = darkColorScheme(
    primary = FossCyanPrimary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF083344),
    onPrimaryContainer = FossCyanLight,
    secondary = FossEmeraldGreen,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = FossEmeraldLight,
    tertiary = FossPurpleAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3B0764),
    onTertiaryContainer = Color(0xFFE9D5FF),
    background = FossDarkBg,
    onBackground = FossTextPrimary,
    surface = FossSurfaceDark,
    onSurface = FossTextPrimary,
    surfaceVariant = FossSurfaceElevated,
    onSurfaceVariant = FossTextSecondary,
    outline = FossBorderMedium,
    outlineVariant = FossBorderSubtle
)

private val FossLightColorScheme = lightColorScheme(
    primary = Color(0xFF0891B2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFFAFE),
    onPrimaryContainer = Color(0xFF164E63),
    secondary = Color(0xFF059669),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = Color(0xFF9333EA),
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek privacy dark theme
    dynamicColor: Boolean = false, // Keep consistent cyber FOSS branding
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) FossDarkColorScheme else FossLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
